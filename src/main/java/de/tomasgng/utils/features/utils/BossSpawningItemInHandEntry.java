package de.tomasgng.utils.features.utils;

import org.bukkit.inventory.ItemStack;

public record BossSpawningItemInHandEntry(boolean isEnabled, ItemStack item, int dropChance) {
}
