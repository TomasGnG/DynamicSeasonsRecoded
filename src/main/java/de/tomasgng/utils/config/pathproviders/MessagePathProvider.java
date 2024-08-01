package de.tomasgng.utils.config.pathproviders;

import de.tomasgng.utils.config.utils.ConfigExclude;
import de.tomasgng.utils.config.utils.ConfigPair;

import java.util.List;

public final class MessagePathProvider {
    public static ConfigPair PREFIX = new ConfigPair("prefix", "<gradient:#55C156:#FFFF00:#FFA500:#87CEFA>DynamicSeasons</gradient> <dark_gray>|");

    public static ConfigPair SEASON_CHANGE_BROADCAST = new ConfigPair("seasonchange.broadcast", "%prefix% <gray>The season was changed from <yellow>%lastSeason% <gray>to <green>%newSeason%<gray>.");
    public static ConfigPair SEASON_CHANGE_TITLE = new ConfigPair("seasonchange.title", "<yellow>Season Change");
    public static ConfigPair SEASON_CHANGE_SUBTITLE = new ConfigPair("seasonchange.subtitle", "<italic><dark_gray>-> <green>%newSeason%");

    public static ConfigPair COMMAND_USAGE = new ConfigPair("command.usage", List.of(
            " ",
            "%prefix% <yellow>Command Usage",
            " ",
            "<yellow>/dynseasons <dark_gray>| <gray>Shows the usage message",
            "<yellow>/dynseasons setseason <New Season> <dark_gray>| <gray>Changes the current season",
            "<yellow>/dynseasons setremainingtime <New Remaining Time> <dark_gray>| <gray>Changes the remaining time for the next season",
            "<yellow>/dynseasons reload <dark_gray>| <gray>Reloads the config files",
            "<yellow>/dynseasons update <dark_gray>| <gray>Updates the plugin to the newest version",
            "<yellow>/dynseasons spawnboss <Seasons> <BossName> <dark_gray>| <gray>Spawns a boss from any season",
            "<yellow>/dynseasons report <Bugreport/feedback> <Message> <dark_gray>| <gray>Send an anonymous feedback to the plugin author",
            " "
    ));
    public static ConfigPair COMMAND_PLAYERONLY = new ConfigPair("command.playerOnly", "%prefix% <red>This command can only be executed by players.");
    public static ConfigPair COMMAND_NO_PERMISSION = new ConfigPair("command.noPermission", "%prefix% <gray>You don't have the permission to execute this command.");
    public static ConfigPair COMMAND_INVALID_SEASON_INPUT = new ConfigPair("command.invalidSeasonInput", "%prefix% <red>Invalid season!");
    public static ConfigPair COMMAND_INVALID_NUMBER_INPUT = new ConfigPair("command.invalidNumberInput", "%prefix% <red>Invalid number!");
    public static ConfigPair COMMAND_SET_SEASON_SUCCESS = new ConfigPair("command.setSeasonSuccess", "%prefix% <green>You changed the season to %newSeason%.");
    public static ConfigPair COMMAND_SET_REMAINING_TIME_SUCCESS = new ConfigPair("command.setRemainingTimeSuccess", "%prefix% <green>You set the remaining time to %remainingTime%.");
    public static ConfigPair COMMAND_RELOAD_SUCCESS = new ConfigPair("command.reloadSuccess", "%prefix% <green>You successfully reloaded the configurations.");
    public static ConfigPair COMMAND_UPDATE_NO_UPDATES_AVAILABLE = new ConfigPair("command.update.noUpdatesAvailable", "%prefix% <green>There are no updates available.");
    public static ConfigPair COMMAND_UPDATE_STARTED = new ConfigPair("command.update.started", "%prefix% <green>Updating...");
    public static ConfigPair COMMAND_UPDATE_SUCCESS = new ConfigPair("command.update.success", "%prefix% <green>Update was successful. Restart server to use the update.");
    public static ConfigPair COMMAND_UPDATE_FAILURE = new ConfigPair("command.update.failure", "%prefix% <red>Update failed. See console for the detailed error.");
    public static ConfigPair COMMAND_RELOAD_WARNINGS = new ConfigPair("command.reloadWarnings", "%prefix% <red>The following warnings and erros occured when reloading:");
    public static ConfigPair COMMAND_SPAWNBOSS_UNKNOWBOSS = new ConfigPair("command.spawnBoss.unknownBoss", "%prefix% <red>Unknown boss!");
    public static ConfigPair COMMAND_SPAWNBOSS_SUCCESS = new ConfigPair("command.spawnBoss.success", "%prefix% <green>You spawned the %boss% boss.");
    public static ConfigPair COMMAND_FEEDBACK_COOLDOWN = new ConfigPair("command.feedback.cooldown", "%prefix% <red>You are allowed to send one feedback per minute.");
    public static ConfigPair COMMAND_FEEDBACK_INVALID_FEEDBACK_TYPE = new ConfigPair("command.feedback.invalidFeedbackType", "%prefix% <red>Invalid feedback type. (Available: Feedback/Bugreport)");
    public static ConfigPair COMMAND_FEEDBACK_SENDING = new ConfigPair("command.feedback.sending", "%prefix% <green>Sending feedback...");
    public static ConfigPair COMMAND_FEEDBACK_SUCCESS = new ConfigPair("command.feedback.success", "%prefix% <green>Your feedback has been sent. Thanks!");
    public static ConfigPair COMMAND_FEEDBACK_FAILURE = new ConfigPair("command.feedback.failure", "%prefix% <red>There was an error while sending your feedback. Check console for more information.");

    @ConfigExclude(excludeComments = false)
    public static ConfigPair COMMENT_PREFIX = new ConfigPair("prefix",
            null,
            "All messages have to be in MiniMessage format.",
            "Guide for MiniMessages: https://docs.advntr.dev/minimessage/index.html",
            "Using legacy color won't work.",
            "The default message will be used if legacy color codes are found.",
            "",
            "Available placeholders:",
            "%lastSeason% -> will be replaced with the last season",
            "%newSeason% -> will be replaced with the new season",
            "%remainingTime% -> will be replaced with the season duration");
}
