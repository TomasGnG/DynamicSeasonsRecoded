package de.tomasgng.utils.features.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;

import java.util.Map;

public record CreatureAttributesEntry(EntityType creature, Map<Attribute, Double> attributes) {
}
