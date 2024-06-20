package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;

import java.util.List;

public final class ConfigPathProvider {
    public static ConfigPair SEASON_DURATION = new ConfigPair("season_duration", 3600, "The duration of the season in seconds.", "3600 = 1 hour -> every 1 hour the season will change.");
    public static ConfigPair WORLDS = new ConfigPair("worlds", List.of("world"), "Specify the worlds where the seasons will work.", "A world not in this list will not be affected by any season changes.");
    public static ConfigPair PLACEHOLDERS_DURATION_PLACERHOLDERNAME = new ConfigPair("placeholders.duration.placeholderName", "duration", "The name of the placeholder which will be used ingame", "Example: 'duration' -> usable by using '%duration%' ingame.");
    public static ConfigPair PLACEHOLDERS_DURATION_FORMAT = new ConfigPair("placeholders.duration.format", "HH:mm:ss", "The format of how the time will be displayed when using the placeholder", "Example: 'HH:mm:ss' -> will be displayed as '01:59:59'");
    public static ConfigPair PLACEHOLDERS_CURRENTSEASON_PLACEHOLDERNAME = new ConfigPair("placeholders.currenseason.placeholderName", "currentseason", "The name of the placeholder which will be used ingame", "Example: 'currentseason' -> usable by using '%currentseason%' ingame.");
    public static ConfigPair PLACEHOLDERS_CURRENTSEASON_TEXT_SPRING = new ConfigPair("placeholders.currenseason.text.spring", "Spring", "The text that will be displayed when the currentseason is changed to this season.");
    public static ConfigPair PLACEHOLDERS_CURRENTSEASON_TEXT_SUMMER = new ConfigPair("placeholders.currenseason.text.summer", "Summer");
    public static ConfigPair PLACEHOLDERS_CURRENTSEASON_TEXT_FALL = new ConfigPair("placeholders.currenseason.text.fall", "Fall");
    public static ConfigPair PLACEHOLDERS_CURRENTSEASON_TEXT_WINTER = new ConfigPair("placeholders.currenseason.text.winter", "Winter");
    public static ConfigPair COMMAND_NAME = new ConfigPair("command.name", "dynamicseasons", "The name of the command to interact with this plugin.");
    public static ConfigPair COMMAND_DESCRIPTION = new ConfigPair("command.description", "Main DynamicSeasons command", "The description of the command.");
    public static ConfigPair COMMAND_PERMISSION = new ConfigPair("command.permission", "dynamicseasons.cmd.use", "The permission to interact with this command.");
    public static ConfigPair COMMAND_ALIAS = new ConfigPair("command.alias", List.of("dynseasons", "seasons"), "The aliases for this command.");

    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_COMMAND = new ConfigPair("command", null, "Customize the main command of this plugin.");
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_PLACEHOLDERS_CURRENTSEASON_TEXT = new ConfigPair("placeholders.currenseason.text", null, "The replacement text that will be shown when using this placeholder.");
    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_PLACEHOLDERS = new ConfigPair("placeholders", null, "Customize the PAPI placeholders from this plugin.", "Will be disabled when PAPI is not installed.");
}
