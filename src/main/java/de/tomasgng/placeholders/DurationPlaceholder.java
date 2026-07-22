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

    private final ConfigDataProvider configDataProvider;
    private final SeasonManager seasonManager;
    private final DynamicSeasons plugin;

    public DurationPlaceholder(ConfigDataProvider configDataProvider, SeasonManager seasonManager, DynamicSeasons plugin) {
        this.configDataProvider = configDataProvider;
        this.seasonManager = seasonManager;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return configDataProvider.getDurationPlaceholderName();
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
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seasonManager.getRemainingTime(), 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(configDataProvider.getDurationPlaceholderFormat());
        return dateTime.format(formatter);
    }
}
