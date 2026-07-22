package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {

    private final SeasonManager seasonManager;

    public BlockSpreadListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void on(BlockSpreadEvent event) {
        seasonManager.getCurrentSeason().handlePreventCropGrowing(event, event.getNewState().getWorld());
    }
}
