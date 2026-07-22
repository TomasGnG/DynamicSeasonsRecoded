package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChangeListener implements Listener {

    private final SeasonManager seasonManager;

    public PlayerExpChangeListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(PlayerExpChangeEvent event) {
        seasonManager.getCurrentSeason().handlePlayerPickupExperienceEvent(event);
    }
}
