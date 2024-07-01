package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class StructureGrowListener implements Listener {

    @EventHandler
    public void on(StructureGrowEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handlePreventCropGrowing(event, event.getWorld());
    }
}
