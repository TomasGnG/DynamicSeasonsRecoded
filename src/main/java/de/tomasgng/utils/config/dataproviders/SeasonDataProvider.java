package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.SeasonDataManager;
import de.tomasgng.utils.enums.SeasonType;

import java.util.Arrays;

import static de.tomasgng.utils.config.pathproviders.SeasonDataPathProvider.SEASON_CURRENT_SEASON;
import static de.tomasgng.utils.config.pathproviders.SeasonDataPathProvider.SEASON_REMAINING_DURATION;

public class SeasonDataProvider {

    private final SeasonDataManager manager;

    public SeasonDataProvider() {
        manager = DynamicSeasons.getInstance().getSeasonDataManager();
    }

    public int getRemainingDuration() {
        return manager.getIntegerValue(SEASON_REMAINING_DURATION);
    }

    public void setRemainingDuration(int newRemainingTime) {
        manager.set(SEASON_REMAINING_DURATION, newRemainingTime);
    }

    public SeasonType getCurrentSeason() {
        String seasonType = manager.getStringValue(SEASON_CURRENT_SEASON);

        return Arrays.stream(SeasonType.values())
                .filter(x -> x.toString().equalsIgnoreCase(seasonType))
                .findFirst()
                .orElse(SeasonType.SPRING);
    }

    public void setCurrentSeason(SeasonType newSeasonType) {
        manager.set(SEASON_CURRENT_SEASON, newSeasonType.name());
    }
}
