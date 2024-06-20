package de.tomasgng.utils.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.ConfigManager;
import de.tomasgng.utils.enums.SeasonType;

import java.text.SimpleDateFormat;
import java.util.List;

import static de.tomasgng.utils.config.pathproviders.ConfigPathProvider.*;

public class ConfigDataProvider {
    private final ConfigManager manager;

    public ConfigDataProvider() {
        manager = DynamicSeasons.getInstance().getConfigManager();
    }

    public int getSeasonDuration() {
        return manager.getIntegerValue(SEASON_DURATION);
    }

    public List<String> getWorlds() {
        return manager.getStringListValue(WORLDS);
    }

    public String getDurationPlaceholderName() {
        return manager.getStringValue(PLACEHOLDERS_DURATION_PLACERHOLDERNAME);
    }

    public String getDurationPlaceholderFormat() {
        String format = manager.getStringValue(PLACEHOLDERS_DURATION_FORMAT);

        try {
            new SimpleDateFormat(format);
        } catch (IllegalArgumentException exception) {
            DynamicSeasons.getInstance().getLogger().severe("Invalid date format! Path in config: " + PLACEHOLDERS_DURATION_FORMAT.getPath());
            format = PLACEHOLDERS_DURATION_FORMAT.getStringValue();
        }

        return format;
    }

    public String getCurrentSeasonPlaceholderName() {
        return manager.getStringValue(PLACEHOLDERS_CURRENTSEASON_PLACEHOLDERNAME);
    }

    public String getCurrentSeasonReplacementText(SeasonType season) {
        String replacement = "";

        switch (season) {
            case SPRING -> replacement = manager.getStringValue(PLACEHOLDERS_CURRENTSEASON_TEXT_SPRING);
            case SUMMER -> replacement = manager.getStringValue(PLACEHOLDERS_CURRENTSEASON_TEXT_SUMMER);
            case FALL -> replacement = manager.getStringValue(PLACEHOLDERS_CURRENTSEASON_TEXT_FALL);
            case WINTER -> replacement = manager.getStringValue(PLACEHOLDERS_CURRENTSEASON_TEXT_WINTER);
        }

        return replacement;
    }

    public String getCommandName() {
        return manager.getStringValue(COMMAND_NAME);
    }

    public String getCommandDescription() {
        return manager.getStringValue(COMMAND_DESCRIPTION);
    }

    public String getCommandPermission() {
        return manager.getStringValue(COMMAND_PERMISSION);
    }

    public List<String> getCommandAliases() {
        return manager.getStringListValue(COMMAND_ALIAS);
    }
}
