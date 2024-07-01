package de.tomasgng.utils.features;

import org.bukkit.Material;
import org.bukkit.TreeType;

import java.util.List;

public record PreventCropGrowingFeature(boolean isEnabled, List<String> preventedCrops) {

    public boolean isPrevented(Material material) {
        return preventedCrops.contains(material.name());
    }

    public boolean isPrevented(TreeType treeType) {
        return preventedCrops.contains(treeType.name());
    }
}
