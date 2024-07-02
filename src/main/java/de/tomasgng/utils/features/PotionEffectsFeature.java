package de.tomasgng.utils.features;

import org.bukkit.potion.PotionEffect;

import java.util.List;

public record PotionEffectsFeature(boolean isEnabled, List<PotionEffect> effects) {
}
