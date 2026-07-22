package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener {

    private final SeasonManager seasonManager;

    public BlockGrowListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(BlockGrowEvent event) {
        seasonManager.getCurrentSeason().handlePreventCropGrowing(event, event.getNewState().getWorld());
    }
}
