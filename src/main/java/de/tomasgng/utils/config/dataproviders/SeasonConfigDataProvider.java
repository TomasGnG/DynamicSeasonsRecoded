package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.ItemBuilder;
import de.tomasgng.utils.PluginLogger;
import de.tomasgng.utils.config.SeasonConfigManager;
import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.features.*;
import de.tomasgng.utils.features.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static de.tomasgng.utils.config.pathproviders.SeasonConfigPathProvider.*;

public class SeasonConfigDataProvider {

    private SeasonConfigManager config = getConfigManager();
    private final PluginLogger logger = PluginLogger.getInstance();

    public WeatherFeature getWeatherFeature() {
        boolean isEnabled = config.getBooleanValue(WEATHER_ENABLED);
        boolean clearWeatherEnabled = config.getBooleanValue(WEATHER_TYPE_CLEAR_ENABLED);
        boolean stormWeatherEnabled = config.getBooleanValue(WEATHER_TYPE_STORM_ENABLED);
        boolean thunderWeatherEnabled = config.getBooleanValue(WEATHER_TYPE_THUNDER_ENABLED);

        return new WeatherFeature(isEnabled, clearWeatherEnabled, stormWeatherEnabled, thunderWeatherEnabled);
    }

    public RandomTickSpeedFeature getRandomTickSpeedFeature() {
        boolean isEnabled = config.getBooleanValue(RANDOM_TICK_SPEED_ENABLED);
        int randomTickSpeed = config.getIntegerValue(RANDOM_TICK_SPEED);

        return new RandomTickSpeedFeature(isEnabled, randomTickSpeed);
    }

    public XPBonusFeature getXPBonusFeature() {
        boolean isEnabled = config.getBooleanValue(XP_BONUS_ENABLED);
        int bonus = config.getIntegerValue(XP_BONUS);

        if(bonus < 0) {
            logger.warn("XP bonus value cannot be negative! Feature will be disabled. Path in config: " + XP_BONUS.getPath());
            isEnabled = false;
        }

        return new XPBonusFeature(isEnabled, bonus);
    }

    public AnimalSpawningFeature getAnimalSpawningFeature() {
        boolean isEnabled = config.getBooleanValue(ANIMAL_SPAWNING_ENABLED);
        Map<String, Object> rawEntries = config.getValuesFromBase(ANIMAL_SPAWNING_ENTRIES_BASE);
        Set<AnimalSpawningEntry> parsedEntries = new HashSet<>();

        rawEntries.forEach((key, value) -> {
            String path = ANIMAL_SPAWNING_ENTRIES_BASE.getPath() + "." + key;

            EntityType animal;
            int spawnChance;

            try {
                animal = EntityType.valueOf(key.toUpperCase());
                spawnChance = Integer.parseInt(value.toString());

                if(spawnChance > 100 || spawnChance < 0)
                    throw new NumberFormatException();
            } catch(NumberFormatException e) {
                logger.warn(value + " is not a valid spawn chance. Must be a number from 0-100. Path in config: " + path);
                return;
            } catch (IllegalArgumentException e) {
                logger.warn(key + " is not a valid animal! Path in config: " + path);
                return;
            }

            parsedEntries.add(new AnimalSpawningEntry(animal, spawnChance));
        });

        return new AnimalSpawningFeature(isEnabled, parsedEntries);
    }

    public CreatureAttributesFeature getCreatureAttributesFeature() {
        boolean isEnabled = config.getBooleanValue(CREATURE_ATTRIBUTES_ENABLED);
        Map<String, Object> rawEntries = config.getValuesFromBase(CREATURE_ATTRIBUTES_ENTRIES_BASE);
        Set<CreatureAttributesEntry> parsedEntries = new HashSet<>();

        rawEntries.forEach((key, value) -> {
            String path = CREATURE_ATTRIBUTES_ENTRIES_BASE.getPath() + "." + key;
            Map<String, Object> rawAttributes = config.getValuesFromBase(new ConfigPair(CREATURE_ATTRIBUTES_ENTRIES_BASE.getPath() + "." + key, null));

            EntityType creature;

            try {
                creature = EntityType.valueOf(key.toUpperCase());
            } catch (Exception ignore) {
                logger.warn(key + " is not a valid entity type! Path in config: " + path);
                return;
            }

            Map<Attribute, Double> attributes = parseAttributes(path, rawAttributes);

            parsedEntries.add(new CreatureAttributesEntry(creature, attributes));
        });

        return new CreatureAttributesFeature(isEnabled, parsedEntries);
    }

    public AnimalGrowingFeature getAnimalGrowingFeature() {
        boolean isEnabled = config.getBooleanValue(ANIMAL_GROWING_ENABLED);
        Map<String, Object> rawEntries = config.getValuesFromBase(ANIMAL_GROWING_ENTRIES_BASE);
        Set<AnimalGrowingEntry> parsedEntries = new HashSet<>();

        rawEntries.forEach((key, value) -> {
            String path = ANIMAL_GROWING_ENTRIES_BASE.getPath() + "." + key;

            EntityType animal;
            int growTime;

            try {
                animal = EntityType.valueOf(key.toUpperCase());
                growTime = Integer.parseInt(value.toString());

                if(growTime < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                logger.warn(value + " is not a valid growing value. Must be a positive number. Path in config: " + path);
                return;
            } catch (IllegalArgumentException ignore) {
                logger.warn(key + " is not a valid animal type! Path in config: " + path);
                return;
            }

            parsedEntries.add(new AnimalGrowingEntry(animal, growTime));
        });

        return new AnimalGrowingFeature(isEnabled, parsedEntries);
    }

    public PreventCropGrowingFeature getPreventCropGrowingFeature() {
        boolean isEnabled = config.getBooleanValue(PREVENT_CROP_GROWING_ENABLED);
        List<String> rawCrops = config.getStringListValue(PREVENT_CROP_GROWING_ENTRIES);
        List<String> parsedCrops = new ArrayList<>();

        for (String rawCrop : rawCrops) {
            String path = PREVENT_CROP_GROWING_ENTRIES.getPath() + "." + rawCrop;

            boolean isMaterial = Arrays.stream(Material.values()).anyMatch(x -> x.name().equalsIgnoreCase(rawCrop));
            boolean isTreeType = Arrays.stream(TreeType.values()).anyMatch(x -> x.name().equalsIgnoreCase(rawCrop));

            if(isMaterial || isTreeType)
                parsedCrops.add(rawCrop.toUpperCase());
            else
                logger.warn(rawCrop + " is not a valid crop. Path in config: " + path);
        }

        return new PreventCropGrowingFeature(isEnabled, parsedCrops);
    }

    public PotionEffectsFeature getPotionEffectsFeature() {
        boolean isEnabled = config.getBooleanValue(POTION_EFFECTS_ENABLED);
        Map<String, Object> rawEntries = config.getValuesFromBase(POTION_EFFECTS_ENTRIES_BASE);
        List<PotionEffect> parsedEntries = new ArrayList<>();

        rawEntries.forEach((key, value) -> {
            String path = POTION_EFFECTS_ENTRIES_BASE.getPath() + "." + key;

            try {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.fromString(key.toLowerCase()));

                if(type == null) {
                    logger.warn(key + " is not a valid potion effect type. Path in config: " + path);
                    return;
                }

                int level = Integer.parseInt(value.toString()) - 1;

                parsedEntries.add(new PotionEffect(type, 8 * 20, level));
            } catch (Exception e) {
                logger.warn(value + " is not a valid effect value. Path in config: " + path);
            }
        });

        return new PotionEffectsFeature(isEnabled, parsedEntries);
    }

    public LootDropsFeature getLootDropsFeature() {
        boolean isEnabled = config.getBooleanValue(LOOT_DROPS_ENABLED);
        Set<String> rawEntityTypes = config.getKeysFromBase(LOOT_DROPS_ENTRIES_BASE);
        Map<EntityType, List<LootDropsEntry>> parsedEntityEntries = new HashMap<>();
        Map<Material, List<LootDropsEntry>> parsedBlockEntries = new HashMap<>();

        for (String rawType : rawEntityTypes) {
            String typePath = LOOT_DROPS_ENTRIES_BASE.getPath() + "." + rawType;

            EntityType entityType = null;
            Material blockType = null;

            try {
                entityType = EntityType.valueOf(rawType.toUpperCase());
            } catch (IllegalArgumentException ignore) {}

            try {
                blockType = Material.valueOf(rawType.toUpperCase());

                if(!blockType.isBlock())
                    blockType = null;
            } catch (IllegalArgumentException ignore) {}

            if(entityType == null && blockType == null) {
                logger.warn(rawType + " is neither a block nor a creature. Path in config: " + typePath);
                continue;
            }

            var itemNameEntries = config.getKeysFromBase(new ConfigPair(typePath));
            List<LootDropsEntry> lootDropsEntries = parseLootDropsEntries(typePath, itemNameEntries);

            if(entityType != null)
                parsedEntityEntries.put(entityType, lootDropsEntries);
            else
                parsedBlockEntries.put(blockType, lootDropsEntries);
        }

        return new LootDropsFeature(isEnabled, parsedEntityEntries, parsedBlockEntries);
    }

    public ParticlesFeature getParticlesFeature() {
        boolean isEnabled = config.getBooleanValue(PARTICLES_ENABLED);
        double offsetX = config.getDoubleValue(PARTICLES_X_OFFSET);
        double offsetY = config.getDoubleValue(PARTICLES_Y_OFFSET);
        double offsetZ = config.getDoubleValue(PARTICLES_Z_OFFSET);
        int spawnTime = config.getIntegerValue(PARTICLES_SPAWNTIME);
        double particleSpeed = config.getDoubleValue(PARTICLES_SPEED);

        if(spawnTime <= 0) {
            logger.warn(spawnTime + " is not a valid spawn time. Value must be greater than 0. Set spawn time to " + PARTICLES_SPAWNTIME.getIntegerValue() + ". Path in config: " + PARTICLES_SPAWNTIME.getPath());
            spawnTime = PARTICLES_SPAWNTIME.getIntegerValue();
        }

        if(particleSpeed < 0) {
            logger.warn(particleSpeed + " is not a valid particle speed. Value must be a positive decimal. Set speed to " + PARTICLES_SPEED.getDoubleValue() + ". Path in config: " + PARTICLES_SPEED.getPath());
            particleSpeed = PARTICLES_SPEED.getDoubleValue();
        }

        Set<String> rawParticleTypes = config.getKeysFromBase(PARTICLES_ENTRIES_BASE);
        List<ParticlesEntry> parsedEntries = new ArrayList<>();

        for (String rawParticleType : rawParticleTypes) {
            String particlePath = PARTICLES_ENTRIES_BASE.getPath() + "." + rawParticleType;

            Particle particleType;

            try {
                particleType = Particle.valueOf(rawParticleType.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn(rawParticleType + " is not a valid particle. Path in config: " + particlePath);
                continue;
            }

            int minSpawnAmount = config.getIntegerValue(new ConfigPair(particlePath + "." + PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE.getPath(), 10));
            int maxSpawnAmount = config.getIntegerValue(new ConfigPair(particlePath + "." + PARTICLES_ENTRIES_MAXSPAWNAMOUNT_BASE.getPath(), 40));

            if(minSpawnAmount > maxSpawnAmount) {
                logger.warn("minSpawnAmount can't be greater than maxSpawnAmount! Path in config: " + particlePath + "." + PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE.getPath());
                continue;
            }

            if(minSpawnAmount < 0) {
                logger.warn("minSpawnAmount must be a positive number. Path in config: " + particlePath + "." + PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE.getPath());
                continue;
            }

            parsedEntries.add(new ParticlesEntry(minSpawnAmount, maxSpawnAmount, particleSpeed, offsetX, offsetY, offsetZ, particleType));
        }

        return new ParticlesFeature(isEnabled, spawnTime, parsedEntries);
    }

    public BossSpawningFeature getBossSpawningFeature() {
        boolean isEnabled = config.getBooleanValue(BOSS_SPAWNING_ENABLED);
        List<BossSpawningEntry> parsedEntries = new ArrayList<>();
        Set<String> rawEntries = config.getKeysFromBase(BOSS_SPAWNING_ENTRIES_BASE);

        for (String bossName : rawEntries) {
            String bossNamePath = BOSS_SPAWNING_ENTRIES_BASE.getPath() + "." + bossName;

            EntityType mobType;
            String rawMobType = config.getStringValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_MOBTYPE_BASE.getPath()));

            try {
                mobType = EntityType.valueOf(rawMobType.toUpperCase());
            } catch (IllegalArgumentException ignore) {
                logger.warn(rawMobType + " is not a valid mob type. Path in config: " + bossNamePath + "." + BOSS_SPAWNING_ENTRIES_MOBTYPE_BASE.getPath());
                continue;
            }

            boolean enabled = config.getBooleanValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ENABLED_BASE.getPath()));
            Component displayname = config.getComponentValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_DISPLAYNAME_BASE.getPath()));
            int spawnChance = config.getIntegerValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_SPAWNCHANCE_BASE.getPath(), 0));

            boolean itemInHandEnabled = config.getBooleanValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_ENABLED_BASE.getPath(), false));
            String itemInHandMaterial = config.getStringValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_MATERIAL_BASE.getPath())).toUpperCase();
            Component itemInHandDisplayname = config.getComponentValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_DISPLAYNAME_BASE.getPath()));
            int itemInHandDropChance = config.getIntegerValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_DROPCHANCE_BASE.getPath(), 0));
            List<Component> itemInHandLore = parseListToComponent(config.getStringListValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_LORE_BASE.getPath())));
            Map<Enchantment, Integer> itemInHandEnchantments = parseEnchantments(bossNamePath, config.getValuesFromBase(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_ENCHANTMENTS_BASE.getPath())));
            BossSpawningItemInHandEntry itemInHandEntry = null;

            Map<String, Object> rawAttributes = config.getValuesFromBase(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ATTRIBUTES_BASE.getPath()));
            Map<Attribute, Double> parsedAttributes = parseAttributes(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ATTRIBUTES_BASE.getPath(), rawAttributes);

            boolean lootDropsEnabled = config.getBooleanValue(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_LOOTDROPS_ENABLED_BASE.getPath()));
            Set<String> lootDropsItemNames = config.getKeysFromBase(new ConfigPair(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_LOOTDROPS_ENTRIES_BASE.getPath()));
            List<LootDropsEntry> lootDropsParsedEntries = parseLootDropsEntries(bossNamePath + "." + BOSS_SPAWNING_ENTRIES_LOOTDROPS_ENTRIES_BASE.getPath(), lootDropsItemNames);

            if(Material.getMaterial(itemInHandMaterial) != null) {
                ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(itemInHandMaterial));

                if(itemInHandDisplayname != null)
                    itemBuilder = itemBuilder.displayname(itemInHandDisplayname);

                itemBuilder = itemBuilder.lore(itemInHandLore)
                                         .addEnchantments(itemInHandEnchantments);

                itemInHandEntry = new BossSpawningItemInHandEntry(itemInHandEnabled, itemBuilder.build(), itemInHandDropChance);
            } else
                logger.warn(itemInHandMaterial + " is not a valid material. Path in config: " + bossNamePath + "." + BOSS_SPAWNING_ENTRIES_ITEMINHAND_MATERIAL_BASE.getPath());

            BossSpawningEntry entry = new BossSpawningEntry(bossName, enabled, mobType, displayname, spawnChance, itemInHandEntry, parsedAttributes, lootDropsEnabled, lootDropsParsedEntries);
            parsedEntries.add(entry);
        }

        return new BossSpawningFeature(isEnabled, parsedEntries);
    }

    private Map<Attribute, Double> parseAttributes(String base, Map<String, Object> rawAttributes) {
        Map<Attribute, Double> attributes = new HashMap<>();

        rawAttributes.forEach((rawAttribute, rawAttributeValue) -> {
            Attribute attribute;
            double attributeValue;

            try {
                attribute = Attribute.valueOf(rawAttribute.toUpperCase());
                attributeValue = Double.parseDouble(rawAttributeValue.toString());
            } catch (NumberFormatException e) {
                logger.warn(rawAttributeValue + " is not a valid attribute value. Must be a (decimal) number. Path in config: " + base);
                return;
            } catch (IllegalArgumentException e) {
                logger.warn(rawAttribute + " is not a valid attribute! Path in config: " + base);
                return;
            }

            attributes.put(attribute, attributeValue);
        });

        return attributes;
    }

    private Map<Enchantment, Integer> parseEnchantments(String currentPath, Map<String, Object> rawEnchantments) {
        HashMap<Enchantment, Integer> parsedEnchantments = new HashMap<>();

        rawEnchantments.forEach((key, value) -> {
            String enchantmentPath = currentPath + "." + LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE.getPath() + "." + key;
            try {
                Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.fromString(key.toLowerCase()));

                if(enchantment == null) {
                    logger.warn(key + " is not a valid enchantment entityType. Path in config: " + enchantmentPath);
                    return;
                }

                int level = Integer.parseInt(value.toString());

                parsedEnchantments.put(enchantment, level);
            } catch (NumberFormatException e) {
                logger.warn(key + " is not a valid enchantment value. Path in config: " + enchantmentPath);
            }
        });

        return parsedEnchantments;
    }

    private List<Component> parseListToComponent(List<String> list) {
        final MiniMessage mm = MiniMessage.miniMessage();

        return list.stream()
                .map(mm::deserializeOrNull)
                .filter(Objects::nonNull)
                .map(x -> x.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();
    }

    private List<LootDropsEntry> parseLootDropsEntries(String base, Set<String> itemNameEntries) {
        List<LootDropsEntry> lootDropsEntries = new ArrayList<>();

        for (String itemName : itemNameEntries) {
            String itemNamePath = base + "." + itemName;

            Material material;
            String rawMaterial = config.getStringValue(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_MATERIAL_BASE.getPath()));

            try {
                material = Material.valueOf(rawMaterial.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn(itemName + " is not a valid material. Path in config: " + itemNamePath);
                continue;
            }

            Component displayname = config.getComponentValue(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_DISPLAYNAME_BASE.getPath()));
            List<Component> loreList = parseListToComponent(config.getStringListValue(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_LORE_BASE.getPath(), new ArrayList<>())));
            int amount = config.getIntegerValue(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_AMOUNT_BASE.getPath(), 1));
            int dropChance = config.getIntegerValue(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_DROPCHANCE_BASE.getPath(), 0));
            Map<String, Object> rawEnchantments = config.getValuesFromBase(new ConfigPair(itemNamePath + "." + LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE.getPath()));
            Map<Enchantment, Integer> parsedEnchantments = parseEnchantments(itemNamePath, rawEnchantments);

            ItemBuilder itemBuilder = new ItemBuilder(material);
            itemBuilder = itemBuilder.setAmount(amount);

            if(displayname != null)
                itemBuilder = itemBuilder.displayname(displayname);

            if(!loreList.isEmpty())
                itemBuilder = itemBuilder.lore(loreList);

            if(!parsedEnchantments.isEmpty())
                itemBuilder = itemBuilder.addEnchantments(parsedEnchantments);

            lootDropsEntries.add(new LootDropsEntry(dropChance, itemBuilder.build()));
        }

        return lootDropsEntries;
    }

    private SeasonConfigManager getConfigManager() {
        if (config == null) {
            config = DynamicSeasons.getInstance().getSeasonConfigManager();
            return config;
        }

        return config;
    }
}
