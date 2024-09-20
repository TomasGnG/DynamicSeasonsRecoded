package de.tomasgng;

import de.tomasgng.commands.DynamicSeasonsCommand;
import de.tomasgng.feedback.FeedbackHandler;
import de.tomasgng.listeners.*;
import de.tomasgng.placeholders.PlaceholderManager;
import de.tomasgng.utils.Metrics;
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
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class DynamicSeasons extends JavaPlugin {

    private static DynamicSeasons INSTANCE;

    private BukkitAudiences adventure;

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
    private FeedbackHandler feedbackHandler;

    @Override
    public void onEnable() {
        INSTANCE = this;

        init();

        VersionChecker.getInstance().isLatestVersion(false);
        placeholderManager.registerAll();
    }

    @Override
    public void onDisable() {
        if(placeholderManager != null)
            placeholderManager.unregisterAll();

        if(adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    private void init() {
        adventure = BukkitAudiences.create(this);

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
        feedbackHandler = new FeedbackHandler();

        Metrics metrics = new Metrics(this, 19158);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));

        registerEvents();
        registerCommand();
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new WeatherChangeListener(), this);
        manager.registerEvents(new ThunderChangeListener(), this);
        manager.registerEvents(new CreatureSpawnListener(), this);
        manager.registerEvents(new PlayerExpChangeListener(), this);
        manager.registerEvents(new BlockGrowListener(), this);
        manager.registerEvents(new BlockSpreadListener(), this);
        manager.registerEvents(new StructureGrowListener(), this);
        manager.registerEvents(new EntityDeathListener(), this);
        manager.registerEvents(new EntityDamageListener(), this);
        manager.registerEvents(new BlockBreakListener(), this);
    }

    private void registerCommand() {
        try {
            final Field bukkitCmdMap = getServer().getClass().getDeclaredField("commandMap");
            bukkitCmdMap.setAccessible(true);

            CommandMap comamndMap = (CommandMap) bukkitCmdMap.get(getServer());

            comamndMap.register("dynamicseasons", new DynamicSeasonsCommand());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe("Couldn't register DynamicSeasons command!");
            getLogger().severe(e.getMessage());
        }
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

    public FeedbackHandler getFeedbackHandler() {
        return feedbackHandler;
    }

    public BukkitAudiences getAdventure() {
        if(adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }

        return adventure;
    }
}
