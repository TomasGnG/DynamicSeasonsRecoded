package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void on(CreatureSpawnEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handleCreatureSpawnEvent(event);
    }
}
