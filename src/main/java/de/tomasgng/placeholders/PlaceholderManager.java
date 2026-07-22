package de.tomasgng.placeholders;

import com.google.inject.Inject;
import de.tomasgng.DynamicSeasons;
import de.tomasgng.interfaces.IPluginLogger;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.Bukkit;

public class PlaceholderManager implements IPlaceholderManager {

    private final ConfigDataProvider configDataProvider;
    private final SeasonManager seasonManager;
    private final DynamicSeasons plugin;

    private Object currentSeasonPlaceholder;
    private Object durationPlaceholder;

    private boolean isPapiInstalled;

    @Inject
    public PlaceholderManager(IPluginLogger pluginLogger,
                              ConfigDataProvider configDataProvider,
                              SeasonManager seasonManager,
                              DynamicSeasons plugin) {
        this.configDataProvider = configDataProvider;
        this.seasonManager = seasonManager;
        this.plugin = plugin;

        isPapiInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        createNewInstances();

        if(!isPapiInstalled)
            pluginLogger.warn("PlaceholderAPI is not installed. Placeholders will be disabled.");
    }

    @Override
    public void registerAll() {
        if(!isPapiInstalled)
            return;

        if(currentSeasonPlaceholder instanceof CurrentSeasonPlaceholder csp)
            csp.register();
        if(durationPlaceholder instanceof DurationPlaceholder dp)
            dp.register();
    }

    @Override
    public void unregisterAll() {
        if(!isPapiInstalled)
            return;

        if(currentSeasonPlaceholder instanceof CurrentSeasonPlaceholder csp)
            csp.unregister();
        if(durationPlaceholder instanceof DurationPlaceholder dp)
            dp.unregister();
    }

    @Override
    public void reloadAll() {
        unregisterAll();
        createNewInstances();
        registerAll();
    }

    private void createNewInstances() {
        if(!isPapiInstalled)
            return;

        currentSeasonPlaceholder = new CurrentSeasonPlaceholder(configDataProvider, seasonManager, plugin);
        durationPlaceholder = new DurationPlaceholder(configDataProvider, seasonManager, plugin);
    }

}
