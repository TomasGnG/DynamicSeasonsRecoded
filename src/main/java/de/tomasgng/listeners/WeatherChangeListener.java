package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private final SeasonManager seasonManager;

    public WeatherChangeListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        seasonManager.getCurrentSeason().handleWeatherChangeEvent(event);
    }

}
