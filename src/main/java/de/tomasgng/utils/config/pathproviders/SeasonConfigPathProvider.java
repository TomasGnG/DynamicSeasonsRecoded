package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;

import java.util.List;

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

    public static ConfigPair PREVENT_CROP_GROWING_ENABLED = new ConfigPair("preventCropGrowing.enabled", true);
    public static ConfigPair PREVENT_CROP_GROWING_ENTRIES = new ConfigPair("preventCropGrowing.entries", List.of("wheat", "birch"));
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_PREVENT_CROP_GROWING = new ConfigPair("preventCropGrowing", null,
            "Every crop that is in this list will not grow.",
            "To prevent trees, you must choose the correct name from this list: https://jd.papermc.io/paper/1.20.6/org/bukkit/TreeType.html#enum-constant-summary");

    public static ConfigPair POTION_EFFECTS_ENABLED = new ConfigPair("potionEffects.enabled", true);
    @ConfigExclude
    public static ConfigPair POTION_EFFECTS_ENTRIES_BASE = new ConfigPair("potionEffects.entries", null);
    public static ConfigPair POTION_EFFECTS_ENTRIES_EXAMPLE1 = new ConfigPair("potionEffects.entries.regeneration", 1);
    public static ConfigPair POTION_EFFECTS_ENTRIES_EXAMPLE2 = new ConfigPair("potionEffects.entries.speed", 1);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_POTION_EFFECTS = new ConfigPair("potionEffects", null,
            "List of all potion effects: https://jd.papermc.io/paper/1.20.6/org/bukkit/potion/PotionEffectType.html#field-summary",
            "Entry format:",
            "potion_effect: potion_amplifier");

    public static ConfigPair LOOT_DROPS_ENABLED = new ConfigPair("lootDrops.enabled", true);
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_BASE = new ConfigPair("lootDrops.entries");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE = new ConfigPair("enchantments");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_DISPLAYNAME_BASE = new ConfigPair("displayname");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_LORE_BASE = new ConfigPair("lore");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_AMOUNT_BASE = new ConfigPair("amount");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_DROPCHANCE_BASE = new ConfigPair("dropChance");
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_DISPLAYNAME = new ConfigPair("lootDrops.entries.zombie.diamond_sword.displayname", "<yellow>Tomas' Legendary Sword");
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_LORE = new ConfigPair("lootDrops.entries.zombie.diamond_sword.lore", List.of("<gray>This sword", "<gray>is <red>dangerous</red>!"));
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_AMOUNT = new ConfigPair("lootDrops.entries.zombie.diamond_sword.amount", 1);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_DROPCHANCE = new ConfigPair("lootDrops.entries.zombie.diamond_sword.dropChance", 10);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_ENCHANTMENTS_1 = new ConfigPair("lootDrops.entries.zombie.diamond_sword.enchantments.sharpness", 3);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_ENCHANTMENTS_2 = new ConfigPair("lootDrops.entries.zombie.diamond_sword.enchantments.unbreaking", 5);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_LOOT_DROPS = new ConfigPair("lootDrops", null,
            "You can add infinite amount of items to an entitytype (as an example: zombie).",
            "List of EntityTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/entity/EntityType.html#enum-constant-summary",
            "List of MaterialTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/Material.html#enum-constant-summary",
            "List of EnchantmentTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/enchantments/Enchantment.html#field-summary",
            "",
            "Format:",
            "entity_type:",
            "  material_type:",
            "    (options): (values)"
    );
}