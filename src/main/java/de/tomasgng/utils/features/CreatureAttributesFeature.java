package de.tomasgng.utils.features;

import de.tomasgng.utils.features.utils.CreatureAttributesEntry;

import java.util.Set;

public record CreatureAttributesFeature(boolean isEnabled, Set<CreatureAttributesEntry> entries) {

}
