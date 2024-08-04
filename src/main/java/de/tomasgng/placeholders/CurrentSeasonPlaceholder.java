package de.tomasgng.placeholders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.season.SeasonManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CurrentSeasonPlaceholder extends PlaceholderExpansion {

    private ConfigDataProvider data;
    private SeasonManager seasonManager;

    public CurrentSeasonPlaceholder() {
        data = DynamicSeasons.getInstance().getConfigDataProvider();
        seasonManager = DynamicSeasons.getInstance().getSeasonManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return data.getCurrentSeasonPlaceholderName();
    }

    @Override
    public @NotNull String getAuthor() {
        return DynamicSeasons.getInstance().getDescription().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return DynamicSeasons.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return data.getCurrentSeasonReplacementText(seasonManager.getCurrentSeason().getSeasonType());
    }
}
