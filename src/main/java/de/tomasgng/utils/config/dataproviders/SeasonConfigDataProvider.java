package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static de.tomasgng.utils.config.pathproviders.SeasonConfigPathProvider.*;

public class SeasonConfigDataProvider {

    private SeasonConfigManager configManager = getConfigManager();
    private final PluginLogger logger = PluginLogger.getInstance();

    public WeatherFeature getWeatherFeature() {
        boolean isEnabled = configManager.getBooleanValue(WEATHER_ENABLED);
        boolean clearWeatherEnabled = configManager.getBooleanValue(WEATHER_TYPE_CLEAR_ENABLED);
        boolean stormWeatherEnabled = configManager.getBooleanValue(WEATHER_TYPE_STORM_ENABLED);
        boolean thunderWeatherEnabled = configManager.getBooleanValue(WEATHER_TYPE_THUNDER_ENABLED);

        return new WeatherFeature(isEnabled, clearWeatherEnabled, stormWeatherEnabled, thunderWeatherEnabled);
    }

    public RandomTickSpeedFeature getRandomTickSpeedFeature() {
        boolean isEnabled = configManager.getBooleanValue(RANDOM_TICK_SPEED_ENABLED);
        int randomTickSpeed = configManager.getIntegerValue(RANDOM_TICK_SPEED);

        return new RandomTickSpeedFeature(isEnabled, randomTickSpeed);
    }

    public XPBonusFeature getXPBonusFeature() {
        boolean isEnabled = configManager.getBooleanValue(XP_BONUS_ENABLED);
        int bonus = configManager.getIntegerValue(XP_BONUS);

        if(bonus < 0) {
            logger.warn("XP bonus value cannot be negative! Feature will be disabled. Path in config: " + XP_BONUS.getPath());
            isEnabled = false;
        }

        return new XPBonusFeature(isEnabled, bonus);
    }

    public AnimalSpawningFeature getAnimalSpawningFeature() {
        boolean isEnabled = configManager.getBooleanValue(ANIMAL_SPAWNING_ENABLED);
        Map<String, Object> rawEntries = configManager.getValuesFromBase(ANIMAL_SPAWNING_ENTRIES_BASE);
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
        boolean isEnabled = configManager.getBooleanValue(CREATURE_ATTRIBUTES_ENABLED);
        Map<String, Object> rawEntries = configManager.getValuesFromBase(CREATURE_ATTRIBUTES_ENTRIES_BASE);
        Set<CreatureAttributesEntry> parsedEntries = new HashSet<>();

        rawEntries.forEach((key, value) -> {
            String path = CREATURE_ATTRIBUTES_ENTRIES_BASE.getPath() + "." + key;
            Map<Attribute, Double> attributes = new HashMap<>();
            Map<String, Object> rawAttributes = configManager.getValuesFromBase(new ConfigPair(CREATURE_ATTRIBUTES_ENTRIES_BASE.getPath() + "." + key, null));

            EntityType creature;

            try {
                creature = EntityType.valueOf(key.toUpperCase());
            } catch (Exception ignore) {
                logger.warn(key + " is not a valid entity type! Path in config: " + path);
                return;
            }

            rawAttributes.forEach((rawAttribute, rawAttributeValue) -> {
                Attribute attribute;
                double attributeValue;

                try {
                    attribute = Attribute.valueOf(rawAttribute.toUpperCase());
                    attributeValue = Double.parseDouble(rawAttributeValue.toString());
                } catch (NumberFormatException e) {
                    logger.warn(rawAttributeValue + " is not a valid attribute value. Must be a (decimal) number. Path in config: " + path);
                    return;
                } catch (IllegalArgumentException e) {
                    logger.warn(rawAttribute + " is not a valid attribute! Path in config: " + path);
                    return;
                }

                attributes.put(attribute, attributeValue);
            });

            parsedEntries.add(new CreatureAttributesEntry(creature, attributes));
        });

        return new CreatureAttributesFeature(isEnabled, parsedEntries);
    }

    public AnimalGrowingFeature getAnimalGrowingFeature() {
        boolean isEnabled = configManager.getBooleanValue(ANIMAL_GROWING_ENABLED);
        Map<String, Object> rawEntries = configManager.getValuesFromBase(ANIMAL_GROWING_ENTRIES_BASE);
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
        boolean isEnabled = configManager.getBooleanValue(PREVENT_CROP_GROWING_ENABLED);
        List<String> rawCrops = configManager.getStringListValue(PREVENT_CROP_GROWING_ENTRIES);
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
        boolean isEnabled = configManager.getBooleanValue(POTION_EFFECTS_ENABLED);
        Map<String, Object> rawEntries = configManager.getValuesFromBase(POTION_EFFECTS_ENTRIES_BASE);
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
        boolean isEnabled = configManager.getBooleanValue(LOOT_DROPS_ENABLED);
        Set<String> rawEntityTypes = configManager.getKeysFromBase(LOOT_DROPS_ENTRIES_BASE);
        Map<EntityType, List<LootDropsEntry>> parsedEntries = new HashMap<>();

        for (String rawEntityType : rawEntityTypes) {
            String entityPath = LOOT_DROPS_ENTRIES_BASE.getPath() + "." + rawEntityType;

            List<LootDropsEntry> lootDropsEntries = new ArrayList<>();
            EntityType entityType;

            try {
                entityType = EntityType.valueOf(rawEntityType.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn(rawEntityType + " is not a valid entity entityType. Path in config: " + entityPath);
                continue;
            }

            var rawMaterialTypes = configManager.getKeysFromBase(new ConfigPair(entityPath));

            for (String rawMaterialType : rawMaterialTypes) {
                String materialPath = entityPath + "." + rawMaterialType;

                Material material;
                try {
                    material = Material.valueOf(rawMaterialType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warn(rawMaterialType + " is not a valid material. Path in config: " + materialPath);
                    continue;
                }

                Component displayname = configManager.getComponentValue(new ConfigPair(materialPath + "." + LOOT_DROPS_ENTRIES_DISPLAYNAME_BASE.getPath()));
                List<Component> loreList = parseListToComponent(configManager.getStringListValue(new ConfigPair(materialPath + "." + LOOT_DROPS_ENTRIES_LORE_BASE.getPath(), new ArrayList<>())));
                int amount = configManager.getIntegerValue(new ConfigPair(materialPath + "." + LOOT_DROPS_ENTRIES_AMOUNT_BASE.getPath(), 1));
                int dropChance = configManager.getIntegerValue(new ConfigPair(materialPath + "." + LOOT_DROPS_ENTRIES_DROPCHANCE_BASE.getPath(), 0));
                Map<String, Object> rawEnchantments = configManager.getValuesFromBase(new ConfigPair(materialPath + "." + LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE.getPath()));
                Map<Enchantment, Integer> parsedEnchantments = new HashMap<>();

                rawEnchantments.forEach((key, value) -> {
                    String enchantmentPath = materialPath + "." + LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE.getPath() + "." + key;
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

                ItemStack itemStack = new ItemStack(material);
                itemStack.setAmount(amount);

                ItemMeta itemMeta = itemStack.getItemMeta();

                if(displayname != null)
                    itemMeta.displayName(displayname.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                if(!loreList.isEmpty())
                    itemMeta.lore(loreList);

                if(!parsedEnchantments.isEmpty())
                    parsedEnchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));

                itemStack.setItemMeta(itemMeta);

                lootDropsEntries.add(new LootDropsEntry(dropChance, itemStack));
            }

            parsedEntries.put(entityType, lootDropsEntries);
        }

        return new LootDropsFeature(isEnabled, parsedEntries);
    }

    public ParticlesFeature getParticlesFeature() {
        boolean isEnabled = configManager.getBooleanValue(PARTICLES_ENABLED);
        double offsetX = configManager.getDoubleValue(PARTICLES_X_OFFSET);
        double offsetY = configManager.getDoubleValue(PARTICLES_Y_OFFSET);
        double offsetZ = configManager.getDoubleValue(PARTICLES_Z_OFFSET);
        int spawnTime = configManager.getIntegerValue(PARTICLES_SPAWNTIME);
        double particleSpeed = configManager.getDoubleValue(PARTICLES_SPEED);

        if(spawnTime <= 0) {
            logger.warn(spawnTime + " is not a valid spawn time. Value must be greater than 0. Set spawn time to " + PARTICLES_SPAWNTIME.getIntegerValue() + ". Path in config: " + PARTICLES_SPAWNTIME.getPath());
            spawnTime = PARTICLES_SPAWNTIME.getIntegerValue();
        }

        if(particleSpeed < 0) {
            logger.warn(particleSpeed + " is not a valid particle speed. Value must be a positive decimal. Set speed to " + PARTICLES_SPEED.getDoubleValue() + ". Path in config: " + PARTICLES_SPEED.getPath());
            particleSpeed = PARTICLES_SPEED.getDoubleValue();
        }

        Set<String> rawParticleTypes = configManager.getKeysFromBase(PARTICLES_ENTRIES_BASE);
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

            int minSpawnAmount = configManager.getIntegerValue(new ConfigPair(particlePath + "." + PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE.getPath(), 10));
            int maxSpawnAmount = configManager.getIntegerValue(new ConfigPair(particlePath + "." + PARTICLES_ENTRIES_MAXSPAWNAMOUNT_BASE.getPath(), 40));

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

    private List<Component> parseListToComponent(List<String> list) {
        final MiniMessage mm = MiniMessage.miniMessage();

        return list.stream()
                .map(mm::deserializeOrNull)
                .filter(Objects::nonNull)
                .map(x -> x.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();
    }

    private SeasonConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = DynamicSeasons.getInstance().getSeasonConfigManager();
            return configManager;
        }

        return configManager;
    }
}
