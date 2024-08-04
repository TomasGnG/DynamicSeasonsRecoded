package de.tomasgng.placeholders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.season.SeasonManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DurationPlaceholder extends PlaceholderExpansion {

    private ConfigDataProvider data;
    private SeasonManager seasonManager;

    public DurationPlaceholder() {
        data = DynamicSeasons.getInstance().getConfigDataProvider();
        seasonManager = DynamicSeasons.getInstance().getSeasonManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return data.getDurationPlaceholderName();
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
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seasonManager.getRemainingTime(), 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(data.getDurationPlaceholderFormat());
        return dateTime.format(formatter);
    }
}
