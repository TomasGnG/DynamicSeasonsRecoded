package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.LootDropsEntry;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

public record LootDropsFeature(boolean isEnabled, Map<EntityType, List<LootDropsEntry>> entries) {

    public List<LootDropsEntry> getDrops(EntityType entityType) {
        if(!entries.containsKey(entityType))
            return null;

        return entries.get(entityType);
    }
}
