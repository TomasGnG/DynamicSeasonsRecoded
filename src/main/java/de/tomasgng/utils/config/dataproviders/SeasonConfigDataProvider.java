package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.PluginLogger;
import de.tomasgng.utils.config.SeasonConfigManager;
import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.features.*;
import de.tomasgng.utils.features.utils.AnimalGrowingEntry;
import de.tomasgng.utils.features.utils.AnimalSpawningEntry;
import de.tomasgng.utils.features.utils.CreatureAttributesEntry;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;

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

    private SeasonConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = DynamicSeasons.getInstance().getSeasonConfigManager();
            return configManager;
        }

        return configManager;
    }
}
