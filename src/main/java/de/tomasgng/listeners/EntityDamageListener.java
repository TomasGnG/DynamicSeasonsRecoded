package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private final SeasonManager seasonManager;

    public EntityDamageListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        seasonManager.getCurrentSeason().handleEntityDamageEvent(event);
    }
}
