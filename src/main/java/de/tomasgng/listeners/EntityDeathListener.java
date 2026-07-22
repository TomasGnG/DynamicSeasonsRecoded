package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private final SeasonManager seasonManager;

    public EntityDeathListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        seasonManager.getCurrentSeason().handleEntityDeathEvent(event);
    }
}
