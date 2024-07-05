package de.tomasgng.utils.features.utils;

import org.bukkit.Particle;

public record ParticlesEntry(int minSpawnAmount, int maxSpawnAmount, double speed, double offsetX, double offsetY, double offsetZ, Particle particle) {
}
