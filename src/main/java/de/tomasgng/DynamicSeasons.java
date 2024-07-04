package de.tomasgng;

import de.tomasgng.commands.DynamicSeasonsCommand;
import de.tomasgng.listeners.*;
import de.tomasgng.placeholders.PlaceholderManager;
import de.tomasgng.utils.VersionChecker;
import de.tomasgng.utils.config.ConfigManager;
import de.tomasgng.utils.config.MessageManager;
import de.tomasgng.utils.config.SeasonConfigManager;
import de.tomasgng.utils.config.SeasonDataManager;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonDataProvider;
import de.tomasgng.utils.season.SeasonManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DynamicSeasons extends JavaPlugin {

    private static DynamicSeasons INSTANCE;

    private ConfigManager configManager;
    private SeasonConfigManager seasonConfigManager;
    private MessageManager messageManager;
    private SeasonDataManager seasonDataManager;

    private ConfigDataProvider configDataProvider;
    private MessageDataProvider messageDataProvider;
    private SeasonDataProvider seasonDataProvider;
    private SeasonConfigDataProvider seasonConfigDataProvider;

    private SeasonManager seasonManager;
    private PlaceholderManager placeholderManager;

    @Override
    public void onEnable() {
        if(!CheckIfServerTypeIsPaper())
            return;

        INSTANCE = this;

        init();

        VersionChecker.getInstance().isLatestVersion(false);
        placeholderManager.registerAll();
    }

    private boolean CheckIfServerTypeIsPaper() {
        try {
            Class.forName(com.destroystokyo.paper.event.player.PlayerJumpEvent.class.getName());
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            getLogger().severe("Paper is required for this plugin. Download paper at: https://papermc.io/downloads/paper");
            getLogger().severe("Disabling this plugin..");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
    }

    @Override
    public void onDisable() {
        if(placeholderManager != null)
            placeholderManager.unregisterAll();
    }

    private void init() {
        configManager = new ConfigManager();
        seasonConfigManager = new SeasonConfigManager();
        messageManager = new MessageManager();
        seasonDataManager = new SeasonDataManager();

        configDataProvider = new ConfigDataProvider();
        messageDataProvider = new MessageDataProvider();
        seasonDataProvider = new SeasonDataProvider();
        seasonConfigDataProvider = new SeasonConfigDataProvider();

        seasonManager = new SeasonManager();
        placeholderManager = new PlaceholderManager();

        registerEvents();

        getServer().getCommandMap().register("dynamicseasons", new DynamicSeasonsCommand());
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new WeatherChangeListener(), this);
        manager.registerEvents(new ThunderChangeListener(), this);
        manager.registerEvents(new CreatureSpawnListener(), this);
        manager.registerEvents(new PlayerPickupExperienceListener(), this);
        manager.registerEvents(new BlockGrowListener(), this);
        manager.registerEvents(new BlockSpreadListener(), this);
        manager.registerEvents(new StructureGrowListener(), this);
        manager.registerEvents(new EntityDeathListener(), this);
    }

    public static DynamicSeasons getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SeasonConfigManager getSeasonConfigManager() {
        return seasonConfigManager;
    }

    public ConfigDataProvider getConfigDataProvider() {
        return configDataProvider;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public MessageDataProvider getMessageDataProvider() {
        return messageDataProvider;
    }

    public SeasonDataManager getSeasonDataManager() {
        return seasonDataManager;
    }

    public SeasonDataProvider getSeasonDataProvider() {
        return seasonDataProvider;
    }

    public SeasonManager getSeasonManager() {
        return seasonManager;
    }

    public SeasonConfigDataProvider getSeasonConfigDataProvider() {
        return seasonConfigDataProvider;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }
}
