package de.tomasgng.listeners;

import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final SeasonManager seasonManager;

    public BlockBreakListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        seasonManager.getCurrentSeason().handleLootDrops(event);
    }
}
