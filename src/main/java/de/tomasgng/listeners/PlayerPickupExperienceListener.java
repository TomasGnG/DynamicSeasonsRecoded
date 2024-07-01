package de.tomasgng.listeners;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPickupExperienceListener implements Listener {

    @EventHandler
    public void on(PlayerPickupExperienceEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handlePlayerPickupExperienceEvent(event);
    }
}
