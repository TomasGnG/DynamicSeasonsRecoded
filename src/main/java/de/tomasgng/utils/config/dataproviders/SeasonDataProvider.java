package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.SeasonDataManager;
import de.tomasgng.utils.enums.SeasonType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.tomasgng.utils.config.pathproviders.SeasonDataPathProvider.*;

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

    public List<String> getPlayersDisabledParticles() {
        return manager.getStringListValue(DISABLED_PARTICLES_LIST);
    }

    public void enableParticlesForPlayer(String playerName) {
        ArrayList<String> disabledParticles = new ArrayList<>(manager.getStringListValue(DISABLED_PARTICLES_LIST));

        disabledParticles.remove(playerName);

        manager.set(DISABLED_PARTICLES_LIST, disabledParticles);
    }

    public void disableParticlesForPlayer(String playerName) {
        ArrayList<String> disabledParticles = new ArrayList<>(manager.getStringListValue(DISABLED_PARTICLES_LIST));

        disabledParticles.add(playerName);

        manager.set(DISABLED_PARTICLES_LIST, disabledParticles);
    }
}
