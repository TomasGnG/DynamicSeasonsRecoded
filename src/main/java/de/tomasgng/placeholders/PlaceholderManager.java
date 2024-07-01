package de.tomasgng.placeholders;

import de.tomasgng.utils.PluginLogger;
import org.bukkit.Bukkit;

public class PlaceholderManager {

    private CurrentSeasonPlaceholder currentSeasonPlaceholder;
    private DurationPlaceholder durationPlaceholder;

    private boolean isPapiInstalled;

    public PlaceholderManager() {
        isPapiInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        createNewInstances();

        if(!isPapiInstalled)
            PluginLogger.getInstance().warn("PlaceholderAPI is not installed. Placeholders will be disabled.");
    }

    public void registerAll() {
        if(!isPapiInstalled)
            return;

        currentSeasonPlaceholder.register();
        durationPlaceholder.register();
    }

    public void unregisterAll() {
        if(!isPapiInstalled)
            return;

        currentSeasonPlaceholder.unregister();
        durationPlaceholder.unregister();
    }

    public void reloadAll() {
        unregisterAll();
        createNewInstances();
        registerAll();
    }

    private void createNewInstances() {
        if(!isPapiInstalled)
            return;

        currentSeasonPlaceholder = new CurrentSeasonPlaceholder();
        durationPlaceholder = new DurationPlaceholder();
    }

}
