package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.ParticlesEntry;

import java.util.List;

public record ParticlesFeature(boolean isEnabled, int spawnTimeInTicks, List<ParticlesEntry> entries) {
}
