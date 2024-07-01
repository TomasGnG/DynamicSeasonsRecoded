package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;

public class ThunderChangeListener implements Listener {

    @EventHandler
    public void on(ThunderChangeEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handleThunderChangeEvent(event);
    }

}
