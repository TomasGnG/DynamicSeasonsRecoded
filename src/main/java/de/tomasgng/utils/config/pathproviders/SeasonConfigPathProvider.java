package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;

public final class SeasonConfigPathProvider {
    public static ConfigPair WEATHER_ENABLED = new ConfigPair("weather.enabled", true);
    public static ConfigPair WEATHER_TYPE_CLEAR_ENABLED = new ConfigPair("weather.weatherType.clear", true);
    public static ConfigPair WEATHER_TYPE_STORM_ENABLED = new ConfigPair("weather.weatherType.storm", true);
    public static ConfigPair WEATHER_TYPE_THUNDER_ENABLED = new ConfigPair("weather.weatherType.thunder", true, "This will be disabled if Storm is set to false.");

    public static ConfigPair RANDOM_TICK_SPEED_ENABLED = new ConfigPair("randomTickSpeed.enabled", true);
    public static ConfigPair RANDOM_TICK_SPEED = new ConfigPair("randomTickSpeed.value", 10, "Default value for RandomTickSpeed is 3.", "Higher value -> plants grow faster");

    public static ConfigPair XP_BONUS_ENABLED = new ConfigPair("xpBonus.enabled", true);
    public static ConfigPair XP_BONUS = new ConfigPair("xpBonus.value", 10, "Bonus XP in percentage.", "For example: 10 means 10% -> the player will get 10% more xp.");

    public static ConfigPair ANIMAL_SPAWNING_ENABLED = new ConfigPair("animalSpawning.enabled", true);
    @ConfigExclude
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_BASE = new ConfigPair("animalSpawning.entries", null);
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_EXAMPLE1 = new ConfigPair("animalSpawning.entries.cow", 75);
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_EXAMPLE2 = new ConfigPair("animalSpawning.entries.chicken", 25);

    public static ConfigPair CREATURE_ATTRIBUTES_ENABLED = new ConfigPair("creatureAttributes.enabled", true);
    @ConfigExclude
    public static ConfigPair CREATURE_ATTRIBUTES_ENTRIES_BASE = new ConfigPair("creatureAttributes.entries", true);
    public static ConfigPair CREATURE_ATTRIBUTES_ENTRIES_EXAMPLE_1_ATTRIBUTE = new ConfigPair("creatureAttributes.entries.zombie.generic_max_health", 40.0);
//    public static ConfigPair CREATURE_ATTRIBUTES_ENTRIES_EXAMPLE_2_ATTRIBUTE = new ConfigPair("creatureAttributes.entries.zombie.", true);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_CREATURE_ATTRIBUTES = new ConfigPair("creatureAttributes", null,
            "List of attributes can be found here: https://jd.papermc.io/paper/1.20.6/org/bukkit/attribute/Attribute.html#enum-constant-summary",
            "For more explanation, default values and so on you should visit this site: https://minecraft.fandom.com/wiki/Attribute#Attributes_available_on_all_living_entities");

    public static ConfigPair ANIMAL_GROWING_ENABLED = new ConfigPair("animalGrowing.enabled", true);
    @ConfigExclude
    public static ConfigPair ANIMAL_GROWING_ENTRIES_BASE = new ConfigPair("animalGrowing.entries", null);
    public static ConfigPair ANIMAL_GROWING_ENTRIES_EXAMPLE1 = new ConfigPair("animalGrowing.entries.cow", 120);
    public static ConfigPair ANIMAL_GROWING_ENTRIES_EXAMPLE2 = new ConfigPair("animalGrowing.entries.sheep", 120);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_ANIMAL_GROWING_ENTRIES_BASE = new ConfigPair("animalGrowing.entries", null, "Growing time of babies in seconds", "Default growing time is 24 minutes.");
}
