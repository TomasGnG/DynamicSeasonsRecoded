package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.enums.SeasonType;

public class SeasonDataPathProvider {
    public static ConfigPair SEASON_REMAINING_DURATION = new ConfigPair("remainingDuration", 3600, "This file is for the saving of the plugin data.", "Only change if you know what you're doing.");
    public static ConfigPair SEASON_CURRENT_SEASON = new ConfigPair("currentSeason", SeasonType.SPRING.name());
}
