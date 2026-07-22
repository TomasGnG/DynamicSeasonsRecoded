package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;

public class ThunderChangeListener implements Listener {

    private final SeasonManager seasonManager;

    public ThunderChangeListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(ThunderChangeEvent event) {
        seasonManager.getCurrentSeason().handleThunderChangeEvent(event);
    }

}
