package de.tomasgng;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.tomasgng.commands.DynamicSeasonsCommand;
import de.tomasgng.listeners.*;
import de.tomasgng.placeholders.IPlaceholderManager;
import de.tomasgng.utils.Metrics;
import de.tomasgng.utils.VersionChecker;
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

    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new DynamicSeasonsModule(this));

        injector.getInstance(VersionChecker.class).isLatestVersion(false);

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            injector.getInstance(IPlaceholderManager.class).registerAll();

        setupMetrics();
        registerEvents();
        registerCommand();
    }

    @Override
    public void onDisable() {
        if(injector == null)
            return;

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            injector.getInstance(IPlaceholderManager.class).unregisterAll();

        injector.getInstance(BukkitAudiences.class).close();
    }

    private void setupMetrics() {
        Metrics metrics = new Metrics(this, 19158);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        SeasonManager seasonManager = injector.getInstance(SeasonManager.class);

        manager.registerEvents(new WeatherChangeListener(seasonManager), this);
        manager.registerEvents(new ThunderChangeListener(seasonManager), this);
        manager.registerEvents(new CreatureSpawnListener(seasonManager), this);
        manager.registerEvents(new PlayerExpChangeListener(seasonManager), this);
        manager.registerEvents(new BlockGrowListener(seasonManager), this);
        manager.registerEvents(new BlockSpreadListener(seasonManager), this);
        manager.registerEvents(new StructureGrowListener(seasonManager), this);
        manager.registerEvents(new EntityDeathListener(seasonManager), this);
        manager.registerEvents(new EntityDamageListener(seasonManager), this);
        manager.registerEvents(new BlockBreakListener(seasonManager), this);
    }

    private void registerCommand() {
        try {
            final Field bukkitCmdMap = getServer().getClass().getDeclaredField("commandMap");
            bukkitCmdMap.setAccessible(true);

            CommandMap comamndMap = (CommandMap) bukkitCmdMap.get(getServer());

            comamndMap.register("dynamicseasons", injector.getInstance(DynamicSeasonsCommand.class));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe("Couldn't register DynamicSeasons command!");
            getLogger().severe(e.getMessage());
        }
    }
}
