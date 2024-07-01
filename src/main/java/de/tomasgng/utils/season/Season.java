package de.tomasgng.utils.season;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonConfigDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import de.tomasgng.utils.features.*;
import de.tomasgng.utils.features.utils.AnimalGrowingEntry;
import de.tomasgng.utils.features.utils.AnimalSpawningEntry;
import de.tomasgng.utils.features.utils.CreatureAttributesEntry;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Season {

    private final SeasonType seasonType;
    private final Random random = new Random();

    private ConfigDataProvider configDataProvider;
    private SeasonConfigDataProvider seasonConfigDataProvider;
    private List<World> worlds = new ArrayList<>();

    private WeatherFeature weatherFeature;
    private RandomTickSpeedFeature randomTickSpeedFeature;
    private AnimalSpawningFeature animalSpawningFeature;
    private CreatureAttributesFeature creatureAttributesFeature;
    private XPBonusFeature xpBonusFeature;
    private AnimalGrowingFeature animalGrowingFeature;
    private PreventCropGrowingFeature preventCropGrowingFeature;

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
    }

    private void initWorlds() {
        List<String> worldNames = configDataProvider.getWorlds();

        for (String worldName : worldNames) {
            World world = Bukkit.getWorld(worldName);

            if(world != null)
                worlds.add(world);
        }
    }

    private void initFeatures() {
        weatherFeature = seasonConfigDataProvider.getWeatherFeature();
        randomTickSpeedFeature = seasonConfigDataProvider.getRandomTickSpeedFeature();
        animalSpawningFeature = seasonConfigDataProvider.getAnimalSpawningFeature();
        creatureAttributesFeature = seasonConfigDataProvider.getCreatureAttributesFeature();
        xpBonusFeature = seasonConfigDataProvider.getXPBonusFeature();
        animalGrowingFeature = seasonConfigDataProvider.getAnimalGrowingFeature();
        preventCropGrowingFeature = seasonConfigDataProvider.getPreventCropGrowingFeature();
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
        handleAnimalSpawning(e);
        handleCreatureAttribute(e);
        handleAnimalGrowing(e);
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

    private boolean isNotWhitelistedWorld(World world) {
        return !worlds.contains(world);
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }
}
