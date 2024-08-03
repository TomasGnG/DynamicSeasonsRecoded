package de.tomasgng.utils.season;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonConfigDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import de.tomasgng.utils.features.*;
import de.tomasgng.utils.features.utils.*;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Season {

    private final SeasonType seasonType;
    private final Random random = new Random();

    private final ConfigDataProvider configDataProvider;
    private final SeasonConfigDataProvider seasonConfigDataProvider;
    private final List<World> worlds = new ArrayList<>();

    private WeatherFeature weatherFeature;
    private RandomTickSpeedFeature randomTickSpeedFeature;
    private AnimalSpawningFeature animalSpawningFeature;
    private CreatureAttributesFeature creatureAttributesFeature;
    private XPBonusFeature xpBonusFeature;
    private AnimalGrowingFeature animalGrowingFeature;
    private PreventCropGrowingFeature preventCropGrowingFeature;
    private PotionEffectsFeature potionEffectsFeature;
    private LootDropsFeature lootDropsFeature;
    private ParticlesFeature particlesFeature;
    private BossSpawningFeature bossSpawningFeature;

    private ScheduledTask potionEffectsTimer;
    private BukkitTask particlesTimer;

    public Season(SeasonType seasonType) {
        this.seasonType = seasonType;
        configDataProvider = DynamicSeasons.getInstance().getConfigDataProvider();
        seasonConfigDataProvider = DynamicSeasons.getInstance().getSeasonConfigDataProvider();
    }

    public void init() {
        initWorlds();
        initFeatures();

        if(randomTickSpeedFeature.isEnabled())
            worlds.forEach(x -> Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), () ->
                    x.setGameRule(GameRule.RANDOM_TICK_SPEED, randomTickSpeedFeature.randomTickSpeed())
            ));

        handlePotionEffects();
        handleParticles();
    }

    private void initWorlds() {
        List<String> worldNames = configDataProvider.getWorlds();

        worlds.clear();

        for (String worldName : worldNames) {
            World world = Bukkit.getWorld(worldName);

            if(world != null)
                worlds.add(world);
        }
    }

    public void initFeatures() {
        weatherFeature = seasonConfigDataProvider.getWeatherFeature();
        randomTickSpeedFeature = seasonConfigDataProvider.getRandomTickSpeedFeature();
        animalSpawningFeature = seasonConfigDataProvider.getAnimalSpawningFeature();
        creatureAttributesFeature = seasonConfigDataProvider.getCreatureAttributesFeature();
        xpBonusFeature = seasonConfigDataProvider.getXPBonusFeature();
        animalGrowingFeature = seasonConfigDataProvider.getAnimalGrowingFeature();
        preventCropGrowingFeature = seasonConfigDataProvider.getPreventCropGrowingFeature();
        potionEffectsFeature = seasonConfigDataProvider.getPotionEffectsFeature();
        lootDropsFeature = seasonConfigDataProvider.getLootDropsFeature();
        particlesFeature = seasonConfigDataProvider.getParticlesFeature();
        bossSpawningFeature = seasonConfigDataProvider.getBossSpawningFeature();
    }

    public void handleWeatherChangeEvent(WeatherChangeEvent e) {
        if(isNotWhitelistedWorld(e.getWorld()))
            return;

        if(e.getCause() == WeatherChangeEvent.Cause.COMMAND)
            return;

        if(!weatherFeature.isEnabled())
            return;

        if(e.toWeatherState())
            e.setCancelled(!weatherFeature.isStormWeatherEnabled());

        if(!e.toWeatherState())
            e.setCancelled(!weatherFeature.isClearWeatherEnabled());
    }

    public void handleThunderChangeEvent(ThunderChangeEvent e) {
        if(isNotWhitelistedWorld(e.getWorld()))
            return;

        if(e.getCause() == ThunderChangeEvent.Cause.COMMAND)
            return;

        if(!weatherFeature.isEnabled())
            return;

        if(e.toThunderState())
            e.setCancelled(!weatherFeature.isThunderWeatherEnabled());

        if(!e.toThunderState())
            e.setCancelled(!weatherFeature.isClearWeatherEnabled());
    }

    public void handleCreatureSpawnEvent(CreatureSpawnEvent e) {
        if(handleBossSpawning(e))
            return;

        handleAnimalSpawning(e);
        handleCreatureAttribute(e);
        handleAnimalGrowing(e);
    }

    private boolean handleBossSpawning(CreatureSpawnEvent event) {
        if(!bossSpawningFeature.isEnabled())
            return false;

        if(bossSpawningFeature.isLivingBoss(event.getEntity()))
            return true;

        LivingEntity entity = event.getEntity();
        String bossName = entity.getPersistentDataContainer().get(BossSpawningFeature.MANUAL_SPAWN_KEY, PersistentDataType.STRING);
        BossSpawningEntry choosenEntry = null;

        if(bossName == null) {
            List<BossSpawningEntry> entries = bossSpawningFeature.getEntries(event.getEntityType());
            int randomNumber = random.nextInt(101);

            if(entries.isEmpty())
                return false;

            for (BossSpawningEntry current : entries) {
                if(!current.shouldSpawnAsBoss(randomNumber))
                    continue;

                choosenEntry = current;
                break;
            }
        } else
            choosenEntry = bossSpawningFeature.getEntryByName(bossName);

        if(choosenEntry == null)
            return false;

        BossSpawningEntry entry = new BossSpawningEntry(choosenEntry);

        entry.setEntity(event.getEntity());
        bossSpawningFeature.addLivingBoss(entry);
        return true;
    }

    private void handleAnimalSpawning(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();

        if(entity instanceof Animals) {
            if(!animalSpawningFeature.isEnabled())
                return;

            if(e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL)
                return;

            AnimalSpawningEntry spawningEntry = animalSpawningFeature.entries().stream().filter(x -> x.animal().equals(entity.getType())).findFirst().orElse(null);

            if(spawningEntry == null)
                return;

            int randomSpawnChance = random.nextInt(0, 101);

            if(randomSpawnChance > spawningEntry.spawnChance())
                e.setCancelled(true);
        }
    }

    private void handleCreatureAttribute(CreatureSpawnEvent e) {
        if(e.isCancelled())
            return;

        if(!creatureAttributesFeature.isEnabled())
            return;

        LivingEntity entity = e.getEntity();
        CreatureAttributesEntry attributeEntry = creatureAttributesFeature.entries()
                .stream()
                .filter(x -> x.creature() == entity.getType())
                .findFirst()
                .orElse(null);

        if(attributeEntry == null)
            return;

        attributeEntry.attributes().forEach((key, value) -> {
            if(entity.getAttribute(key) != null)
                entity.getAttribute(key).setBaseValue(value);

            if(key == Attribute.GENERIC_MAX_HEALTH)
                entity.setHealth(value);
        });
    }

    private void handleAnimalGrowing(CreatureSpawnEvent e) {
        if(e.isCancelled())
            return;

        if(!animalGrowingFeature.isEnabled())
            return;

        LivingEntity entity = e.getEntity();

        if(!(entity instanceof Breedable breedable))
            return;

        AnimalGrowingEntry entry = animalGrowingFeature.getEntry(entity.getType());

        if(entry == null)
            return;

        if(entry.growTimeInSeconds() == 0) {
            breedable.setAdult();
            return;
        }

        Bukkit.getAsyncScheduler().runDelayed(
                DynamicSeasons.getInstance(),
                scheduledTask -> breedable.setAdult(),
                entry.growTimeInSeconds(),
                TimeUnit.SECONDS);
    }

    public void handlePlayerPickupExperienceEvent(PlayerPickupExperienceEvent e) {
        if(!xpBonusFeature.isEnabled())
            return;

        double bonus = (xpBonusFeature.xpBonus() / 100.0) + 1;
        int newXP = BigDecimal.valueOf(e.getExperienceOrb().getExperience() * bonus).setScale(0, RoundingMode.HALF_UP).intValue();

        e.getExperienceOrb().setExperience(newXP);
    }

    public void handlePreventCropGrowing(Event event, World world) {
        if(isNotWhitelistedWorld(world))
            return;

        if(!preventCropGrowingFeature.isEnabled())
            return;

        switch (event) {
            case BlockSpreadEvent blockSpreadEvent:
                if(preventCropGrowingFeature.isPrevented(blockSpreadEvent.getNewState().getType()))
                    blockSpreadEvent.setCancelled(true);
                return;
            case BlockGrowEvent blockGrowEvent:
                if(preventCropGrowingFeature.isPrevented(blockGrowEvent.getNewState().getType()))
                    blockGrowEvent.setCancelled(true);
                return;
            case StructureGrowEvent structureGrowEvent:
                if(preventCropGrowingFeature.isPrevented(structureGrowEvent.getSpecies()))
                    structureGrowEvent.setCancelled(true);
                return;
            default:
                return;
        }
    }

    public void handlePotionEffects() {
        if(potionEffectsTimer != null)
            potionEffectsTimer.cancel();

        potionEffectsTimer = Bukkit.getAsyncScheduler().runAtFixedRate(DynamicSeasons.getInstance(), scheduledTask -> {
            if(scheduledTask.isCancelled())
                return;

            if(!potionEffectsFeature.isEnabled())
                return;

            for (World world : worlds) {
                world.getPlayers().forEach(this::givePlayerPotionEffects);
            }
        }, 3, 5, TimeUnit.SECONDS);
    }

    private void givePlayerPotionEffects(Player player) {
        Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), () -> {
            for (PotionEffect effect : potionEffectsFeature.effects()) {
                if(!player.hasPotionEffect(effect.getType()))
                    player.addPotionEffect(effect);
                else if(player.getPotionEffect(effect.getType()).getAmplifier() <= effect.getAmplifier())
                    player.addPotionEffect(effect);
            }
        });
    }

    public void handleEntityDeathEvent(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if(bossSpawningFeature.isLivingBoss(entity)) {
            e.getDrops().addAll(bossSpawningFeature.getLivingBossEntry(entity).getLootDrops());
            bossSpawningFeature.removeLivingBoss(entity);
            return;
        }

        handleLootDrops(e);
    }

    public void handleLootDrops(EntityDeathEvent e) {
        if(!lootDropsFeature.isEnabled())
            return;

        EntityType entityType = e.getEntityType();
        List<LootDropsEntry> drops = lootDropsFeature.getDrops(entityType);

        if(drops == null)
            return;

        for (LootDropsEntry entry : drops) {
            int randomSpawnChance = random.nextInt(1, 101);

            if(randomSpawnChance <= entry.dropChance())
                e.getDrops().add(entry.itemStack());
        }
    }

    public void handleLootDrops(BlockBreakEvent e) {
        if(!lootDropsFeature.isEnabled())
            return;

        Material material = e.getBlock().getType();
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        World world = e.getBlock().getWorld();
        Location location = e.getBlock().getLocation();

        if(!material.createBlockData().isPreferredTool(tool))
            return;

        List<LootDropsEntry> drops = lootDropsFeature.getDrops(material);

        if(drops == null)
            return;

        for (LootDropsEntry entry : drops) {
            int randomSpawnChance = random.nextInt(1, 101);

            if(randomSpawnChance > entry.dropChance())
                continue;

            world.dropItemNaturally(location, entry.itemStack());
        }
    }

    public void handleParticles() {
        if(particlesTimer != null)
            particlesTimer.cancel();

        particlesTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(DynamicSeasons.getInstance(), () -> {
            if(!particlesFeature.isEnabled())
                return;

            List<Player> players = new ArrayList<>();
            worlds.forEach(world -> players.addAll(world.getPlayers()));

            for (ParticlesEntry entry : particlesFeature.entries()) {
                ParticleBuilder particleBuilder = new ParticleBuilder(entry.particle())
                        .count(random.nextInt(entry.minSpawnAmount(), entry.maxSpawnAmount()+1))
                        .extra(entry.speed())
                        .offset(entry.offsetX(), entry.offsetY(), entry.offsetZ());

                players.forEach(player -> particleBuilder.receivers(player).location(player.getLocation()).spawn());
            }
        }, particlesFeature.spawnTimeInTicks(), particlesFeature.spawnTimeInTicks());
    }

    public void handleEntityDamageEvent(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof LivingEntity entity))
            return;

        if(bossSpawningFeature.isLivingBoss(entity))
            bossSpawningFeature.getLivingBossEntry(entity).applyDisplayname();
    }

    private boolean isNotWhitelistedWorld(World world) {
        return !worlds.contains(world);
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }

    public BossSpawningFeature getBossSpawningFeature() {
        return bossSpawningFeature;
    }

    public void stopAllTimers() {
        potionEffectsTimer.cancel();
        particlesTimer.cancel();
    }
}
