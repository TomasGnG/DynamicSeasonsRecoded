package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    private final SeasonManager seasonManager;

    public CreatureSpawnListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(CreatureSpawnEvent event) {
        seasonManager.getCurrentSeason().handleCreatureSpawnEvent(event);
    }
}
