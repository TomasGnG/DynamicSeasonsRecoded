package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;
import org.bukkit.Material;

import java.util.List;

public final class SeasonConfigPathProvider {
    public static ConfigPair WEATHER_ENABLED = new ConfigPair("weather.enabled", true);
    public static ConfigPair WEATHER_TYPE_CLEAR_ENABLED = new ConfigPair("weather.weatherType.clear", true);
    public static ConfigPair WEATHER_TYPE_STORM_ENABLED = new ConfigPair("weather.weatherType.storm", true);
    public static ConfigPair WEATHER_TYPE_THUNDER_ENABLED = new ConfigPair("weather.weatherType.thunder",
                                                                           true,
                                                                           "This will be disabled if Storm is set to false.");

    public static ConfigPair RANDOM_TICK_SPEED_ENABLED = new ConfigPair("randomTickSpeed.enabled", true);
    public static ConfigPair RANDOM_TICK_SPEED = new ConfigPair("randomTickSpeed.value",
                                                                10,
                                                                "Default value for RandomTickSpeed is 3.",
                                                                "Higher value -> plants grow faster");

    public static ConfigPair XP_BONUS_ENABLED = new ConfigPair("xpBonus.enabled", true);
    public static ConfigPair XP_BONUS = new ConfigPair("xpBonus.value",
                                                       10,
                                                       "Bonus XP in percentage.",
                                                       "For example: 10 means 10% -> the player will get 10% more xp.");

    public static ConfigPair ANIMAL_SPAWNING_ENABLED = new ConfigPair("animalSpawning.enabled", true);
    @ConfigExclude
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_BASE = new ConfigPair("animalSpawning.entries", null);
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_EXAMPLE1 = new ConfigPair("animalSpawning.entries.cow", 75);
    public static ConfigPair ANIMAL_SPAWNING_ENTRIES_EXAMPLE2 = new ConfigPair("animalSpawning.entries.chicken", 25);

    public static ConfigPair CREATURE_ATTRIBUTES_ENABLED = new ConfigPair("creatureAttributes.enabled", true);
    @ConfigExclude
    public static ConfigPair CREATURE_ATTRIBUTES_ENTRIES_BASE = new ConfigPair("creatureAttributes.entries", true);
    public static ConfigPair CREATURE_ATTRIBUTES_ENTRIES_EXAMPLE_1_ATTRIBUTE = new ConfigPair(
            "creatureAttributes.entries.zombie.generic_max_health",
            40.0);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_CREATURE_ATTRIBUTES = new ConfigPair("creatureAttributes",
                                                                          null,
                                                                          "List of attributes can be found here: https://jd.papermc.io/paper/1.20.6/org/bukkit/attribute/Attribute.html#enum-constant-summary",
                                                                          "For more explanation, default values and so on you should visit this site: https://minecraft.fandom.com/wiki/Attribute#Attributes_available_on_all_living_entities");

    public static ConfigPair ANIMAL_GROWING_ENABLED = new ConfigPair("animalGrowing.enabled", true);
    @ConfigExclude
    public static ConfigPair ANIMAL_GROWING_ENTRIES_BASE = new ConfigPair("animalGrowing.entries", null);
    public static ConfigPair ANIMAL_GROWING_ENTRIES_EXAMPLE1 = new ConfigPair("animalGrowing.entries.cow", 120);
    public static ConfigPair ANIMAL_GROWING_ENTRIES_EXAMPLE2 = new ConfigPair("animalGrowing.entries.sheep", 120);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_ANIMAL_GROWING_ENTRIES_BASE = new ConfigPair("animalGrowing.entries",
                                                                                  null,
                                                                                  "Growing time of babies in seconds",
                                                                                  "Default growing time is 24 minutes.");

    public static ConfigPair PREVENT_CROP_GROWING_ENABLED = new ConfigPair("preventCropGrowing.enabled", true);
    public static ConfigPair PREVENT_CROP_GROWING_ENTRIES = new ConfigPair("preventCropGrowing.entries",
                                                                           List.of("wheat", "birch"));
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_PREVENT_CROP_GROWING = new ConfigPair("preventCropGrowing",
                                                                           null,
                                                                           "Every crop that is in this list will not grow.",
                                                                           "To prevent trees, you must choose the correct name from this list: https://jd.papermc.io/paper/1.20.6/org/bukkit/TreeType.html#enum-constant-summary");

    public static ConfigPair POTION_EFFECTS_ENABLED = new ConfigPair("potionEffects.enabled", true);
    @ConfigExclude
    public static ConfigPair POTION_EFFECTS_ENTRIES_BASE = new ConfigPair("potionEffects.entries", null);
    public static ConfigPair POTION_EFFECTS_ENTRIES_EXAMPLE1 = new ConfigPair("potionEffects.entries.regeneration", 1);
    public static ConfigPair POTION_EFFECTS_ENTRIES_EXAMPLE2 = new ConfigPair("potionEffects.entries.speed", 1);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_POTION_EFFECTS = new ConfigPair("potionEffects",
                                                                     null,
                                                                     "List of all potion effects: https://jd.papermc.io/paper/1.20.6/org/bukkit/potion/PotionEffectType.html#field-summary",
                                                                     "Entry format:",
                                                                     "potion_effect: potion_amplifier");

    public static ConfigPair LOOT_DROPS_ENABLED = new ConfigPair("lootDrops.enabled", true);
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_BASE = new ConfigPair("lootDrops.entries");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_MATERIAL_BASE = new ConfigPair("material");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_DISPLAYNAME_BASE = new ConfigPair("displayname");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_ENCHANTMENTS_BASE = new ConfigPair("enchantments");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_LORE_BASE = new ConfigPair("lore");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_AMOUNT_BASE = new ConfigPair("amount");
    @ConfigExclude
    public static ConfigPair LOOT_DROPS_ENTRIES_DROPCHANCE_BASE = new ConfigPair("dropChance");
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_MATERIAL = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.material",
            "diamond_sword");
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_DISPLAYNAME = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.displayname",
            "<yellow>Tomas' Legendary Sword");
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_LORE = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.lore",
            List.of("<gray>This sword", "<gray>is <red>dangerous</red>!"));
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_AMOUNT = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.amount",
            1);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_DROPCHANCE = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.dropChance",
            10);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_ENCHANTMENTS_1 = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.enchantments.sharpness",
            3);
    public static ConfigPair LOOT_DROPS_ENTRIES_EXAMPLE_ENCHANTMENTS_2 = new ConfigPair(
            "lootDrops.entries.zombie.ExampleSword.enchantments.unbreaking",
            5);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_LOOT_DROPS = new ConfigPair("lootDrops",
                                                                 null,
                                                                 "You can add infinite amount of items to an entitytype (as an example: zombie).",
                                                                 "List of EntityTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/entity/EntityType.html#enum-constant-summary",
                                                                 "List of MaterialTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/Material.html#enum-constant-summary",
                                                                 "List of EnchantmentTypes: https://jd.papermc.io/paper/1.20.6/org/bukkit/enchantments/Enchantment.html#field-summary",
                                                                 "",
                                                                 "Format:",
                                                                 "entity_type:",
                                                                 "  material_type:",
                                                                 "    (options): (values)");

    public static ConfigPair PARTICLES_ENABLED = new ConfigPair("particles.enabled", true);
    public static ConfigPair PARTICLES_X_OFFSET = new ConfigPair("particles.offset.x", 5.0);
    public static ConfigPair PARTICLES_Y_OFFSET = new ConfigPair("particles.offset.y", 10.0);
    public static ConfigPair PARTICLES_Z_OFFSET = new ConfigPair("particles.offset.z", 5.0);
    public static ConfigPair PARTICLES_SPAWNTIME = new ConfigPair("particles.spawnTime",
                                                                  5,
                                                                  "Time in ticks (20 ticks = 1 second)");
    public static ConfigPair PARTICLES_SPEED = new ConfigPair("particles.speed", 0.0, "Speed of the particles");
    @ConfigExclude
    public static ConfigPair PARTICLES_ENTRIES_BASE = new ConfigPair("particles.entries", null);
    @ConfigExclude
    public static ConfigPair PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE = new ConfigPair("minSpawnAmount", null);
    @ConfigExclude
    public static ConfigPair PARTICLES_ENTRIES_MAXSPAWNAMOUNT_BASE = new ConfigPair("maxSpawnAmount", null);
    public static ConfigPair PARTICLES_ENTRIES_EXAMPLE_MINSPAWNAMOUNT = new ConfigPair("particles.entries.snowflake." + PARTICLES_ENTRIES_MINSPAWNAMOUNT_BASE.getPath(),
                                                                                       10);
    public static ConfigPair PARTICLES_ENTRIES_EXAMPLE_MAXSPAWNAMOUNT = new ConfigPair("particles.entries.snowflake." + PARTICLES_ENTRIES_MAXSPAWNAMOUNT_BASE.getPath(),
                                                                                       50);
    @ConfigExclude
    public static ConfigPair COMMENT_PARTICLES_OFFSET = new ConfigPair("particles.offset",
                                                                       null,
                                                                       "Spread the spawned in all directions.",
                                                                       "As an example: 5 means it will spread the spawned particles within 5 blocks.");

    public static ConfigPair BOSS_SPAWNING_ENABLED = new ConfigPair("bossSpawning.enabled", true);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_BASE = new ConfigPair("bossSpawning.entries", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ENABLED_BASE = new ConfigPair("enabled", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_MOBTYPE_BASE = new ConfigPair("mobType", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_DISPLAYNAME_BASE = new ConfigPair("displayname", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_SPAWNCHANCE_BASE = new ConfigPair("spawnChance", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_ENABLED_BASE = new ConfigPair("itemInHand.enabled", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_MATERIAL_BASE = new ConfigPair("itemInHand.material", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_DISPLAYNAME_BASE = new ConfigPair("itemInHand.displayname", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_DROPCHANCE_BASE = new ConfigPair("itemInHand.dropChance", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_LORE_BASE = new ConfigPair("itemInHand.lore", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ITEMINHAND_ENCHANTMENTS_BASE = new ConfigPair("itemInHand.enchantments", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_ATTRIBUTES_BASE = new ConfigPair("attributes", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_LOOTDROPS_ENABLED_BASE = new ConfigPair("lootDrops.enabled", null);
    @ConfigExclude
    public static ConfigPair BOSS_SPAWNING_ENTRIES_LOOTDROPS_ENTRIES_BASE = new ConfigPair("lootDrops.entries", null);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ENABLED = new ConfigPair("bossSpawning.entries.EasyZombie.enabled", true);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_MOBTYPE = new ConfigPair("bossSpawning.entries.EasyZombie.mobType", "zombie");
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_DISPLAYNAME = new ConfigPair("bossSpawning.entries.EasyZombie.displayname", "<green>E-Tier BOSS <gray>| <green>%health%<gray>/<red>%maxhealth%");
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_SPAWNCHANCE = new ConfigPair("bossSpawning.entries.EasyZombie.spawnChance", 80);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_ENABLED = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.enabled", true);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_MATERIAL = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.material", Material.IRON_SWORD.name());
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_DISPLAYNAME = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.displayname", "<green>E-Tier Sword");
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_DROPCHANCE = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.dropChance", 70);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_LORE = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.lore", List.of("<gray>The sword of a fallen E-Tier boss."));
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_ENCHANTMENTS = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.enchantments" + ".sharpness", 1);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_ENCHANTMENTS_1 = new ConfigPair("bossSpawning.entries.EasyZombie.itemInHand.enchantments" + ".unbreaking", 1);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ATTRIBUTES = new ConfigPair("bossSpawning.entries.EasyZombie.attributes.generic_max_health", 30);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_ATTRIBUTES_1 = new ConfigPair("bossSpawning.entries.EasyZombie.attributes.generic_attack_damage", 3);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_ENABLED = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.enabled", true);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_MATERIAL = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.material", "golden_apple");
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_DISPLAYNAME = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.displayname", "<green>Golden Apple");
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_DROPCHANCE = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.dropChance", 60);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_AMOUNT = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.amount", 1);
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_LORE = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.lore", List.of("<gray>Dropped by <green>E-Tier bosses"));
    public static ConfigPair BOSS_SPAWNING_ENTRIES_EXAMPLE_LOOTDROPS_ENCHANTMENTS = new ConfigPair("bossSpawning.entries.EasyZombie.lootDrops.entries.ExampleItem.enchantments.mending", 1);
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_BOSS_SPAWNING_ENTRIES_ENTRYNAME = new ConfigPair("bossSpawning.entries.EasyZombie", null, "The name of the boss entry. Used to specify all the entries.", "will be used in the spawnboss subcommand");
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_BOSS_SPAWNING_ENTRIES_ENABLED = new ConfigPair(BOSS_SPAWNING_ENTRIES_EXAMPLE_ENABLED.getPath(), null, "false = boss will not spawn");
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_BOSS_SPAWNING_ENTRIES_SPAWNCHANCE = new ConfigPair(BOSS_SPAWNING_ENTRIES_EXAMPLE_SPAWNCHANCE.getPath(), null, "The chance that this boss will spawn", "0 - 100");
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_BOSS_SPAWNING_ENTRIES_ITEMINHAND_ENABLED = new ConfigPair(BOSS_SPAWNING_ENTRIES_EXAMPLE_ITEMINHAND_ENABLED.getPath(), null, "false = boss wont have any item in his hand");
}