package de.tomasgng.listeners;

import de.tomasgng.DynamicSeasons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener {

    @EventHandler
    public void on(BlockGrowEvent event) {
        DynamicSeasons.getInstance().getSeasonManager().getCurrentSeason().handlePreventCropGrowing(event, event.getNewState().getWorld());
    }
}
