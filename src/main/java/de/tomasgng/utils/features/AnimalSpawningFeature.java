package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.AnimalSpawningEntry;

import java.util.Set;

public record AnimalSpawningFeature(boolean isEnabled, Set<AnimalSpawningEntry> entries) {

}
