package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChangeListener implements Listener {

    @EventHandler
    public void on(PlayerExpChangeEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handlePlayerPickupExperienceEvent(event);
    }
}
