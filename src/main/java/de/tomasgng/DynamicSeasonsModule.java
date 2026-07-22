package de.tomasgng;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.tomasgng.commands.DynamicSeasonsCommand;
import de.tomasgng.feedback.FeedbackHandler;
import de.tomasgng.interfaces.IPluginLogger;
import de.tomasgng.placeholders.DummyPlaceholderManager;
import de.tomasgng.placeholders.IPlaceholderManager;
import de.tomasgng.placeholders.PlaceholderManager;
import de.tomasgng.utils.PluginLogger;
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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public class DynamicSeasonsModule extends AbstractModule {

    private final DynamicSeasons plugin;

    public DynamicSeasonsModule(DynamicSeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(DynamicSeasons.class).toInstance(plugin);
        bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(plugin));
        bind(MiniMessage.class).toInstance(MiniMessage.miniMessage());
        bind(String.class).annotatedWith(Names.named("pluginVersion")).toInstance(plugin.getDescription().getVersion());

        bind(IPluginLogger.class).to(PluginLogger.class).asEagerSingleton();
        bind(VersionChecker.class).asEagerSingleton();

        bind(ConfigDataProvider.class).asEagerSingleton();
        bind(MessageDataProvider.class).asEagerSingleton();
        bind(SeasonConfigDataProvider.class).asEagerSingleton();
        bind(SeasonDataProvider.class).asEagerSingleton();

        bind(ConfigManager.class).asEagerSingleton();
        bind(MessageManager.class).asEagerSingleton();
        bind(SeasonConfigManager.class).asEagerSingleton();
        bind(SeasonDataManager.class).asEagerSingleton();

        bind(SeasonManager.class).asEagerSingleton();
        bind(FeedbackHandler.class).asEagerSingleton();
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            bind(PlaceholderManager.class).asEagerSingleton();
            bind(IPlaceholderManager.class).to(PlaceholderManager.class).asEagerSingleton();
        } else {
            bind(IPlaceholderManager.class).to(DummyPlaceholderManager.class).asEagerSingleton();
        }

        bind(DynamicSeasonsCommand.class).asEagerSingleton();
    }
}
