package de.tomasgng.placeholders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.season.SeasonManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CurrentSeasonPlaceholder extends PlaceholderExpansion {

    private final ConfigDataProvider configDataProvider;
    private final SeasonManager seasonManager;
    private final DynamicSeasons plugin;

    public CurrentSeasonPlaceholder(ConfigDataProvider configDataProvider, SeasonManager seasonManager, DynamicSeasons plugin) {
        this.configDataProvider = configDataProvider;
        this.seasonManager = seasonManager;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return configDataProvider.getCurrentSeasonPlaceholderName();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return configDataProvider.getCurrentSeasonReplacementText(seasonManager.getCurrentSeason().getSeasonType());
    }
}
