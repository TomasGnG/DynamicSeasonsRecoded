package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class StructureGrowListener implements Listener {

    private final SeasonManager seasonManager;

    public StructureGrowListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(StructureGrowEvent event) {
        seasonManager.getCurrentSeason().handlePreventCropGrowing(event, event.getWorld());
    }
}
