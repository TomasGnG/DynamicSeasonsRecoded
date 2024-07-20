package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void on(EntityDamageEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handleEntityDamageEvent(event);
    }
}
