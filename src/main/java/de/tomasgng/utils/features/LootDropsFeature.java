package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.LootDropsEntry;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

public record LootDropsFeature(boolean isEnabled, Map<EntityType, List<LootDropsEntry>> entityEntries, Map<Material, List<LootDropsEntry>> blockEntries) {
    public List<LootDropsEntry> getDrops(EntityType entityType) {
        if(!entityEntries.containsKey(entityType))
            return null;

        return entityEntries.get(entityType);
    }
    public List<LootDropsEntry> getDrops(Material material) {
        if(!blockEntries.containsKey(material))
            return null;

        return blockEntries.get(material);
    }
}
