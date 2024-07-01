package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.AnimalGrowingEntry;
import org.bukkit.entity.EntityType;

import java.util.Set;

public record AnimalGrowingFeature(boolean isEnabled, Set<AnimalGrowingEntry> entries) {

    public AnimalGrowingEntry getEntry(EntityType type) {
        return entries.stream().filter(x -> x.animal() == type).findFirst().orElse(null);
    }
}
