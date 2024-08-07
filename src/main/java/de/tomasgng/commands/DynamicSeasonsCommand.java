package de.tomasgng.commands;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.feedback.Feedback;
import de.tomasgng.feedback.FeedbackHandler;
import de.tomasgng.feedback.FeedbackType;
import de.tomasgng.utils.PluginLogger;
import de.tomasgng.utils.PluginUpdater;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import de.tomasgng.utils.features.BossSpawningFeature;
import de.tomasgng.utils.season.SeasonManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DynamicSeasonsCommand extends Command {

    private final ConfigDataProvider configDataProvider;
    private final MessageDataProvider messageDataProvider;
    private final SeasonDataProvider seasonDataProvider;
    private final SeasonManager seasonManager;
    private final FeedbackHandler feedbackHandler;

    private CommandSender sender;
    private String[] args;

    public DynamicSeasonsCommand() {
        super(DynamicSeasons.getInstance().getConfigDataProvider().getCommandName(),
                DynamicSeasons.getInstance().getConfigDataProvider().getCommandDescription(),
                "",
                DynamicSeasons.getInstance().getConfigDataProvider().getCommandAliases());

        configDataProvider = DynamicSeasons.getInstance().getConfigDataProvider();
        messageDataProvider = DynamicSeasons.getInstance().getMessageDataProvider();
        seasonDataProvider = DynamicSeasons.getInstance().getSeasonDataProvider();
        seasonManager = DynamicSeasons.getInstance().getSeasonManager();
        feedbackHandler = DynamicSeasons.getInstance().getFeedbackHandler();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;

        if(checkDisableParticles())
            return false;

        if(!sender.hasPermission(configDataProvider.getCommandPermission())) {
            sender.sendMessage(messageDataProvider.getCommandNoPermission());
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage(messageDataProvider.getCommandUsage());
            return false;
        }

        if(checkReload())
            return false;

        if(checkUpdate())
            return false;

        if(checkSetSeason())
            return false;

        if(checkSetRemainingTime())
            return false;

        if(checkSpawnBoss())
            return false;

        if(checkReport())
            return false;

        sender.sendMessage(messageDataProvider.getCommandUsage());
        return false;
    }

    private boolean checkDisableParticles() {
        if(args.length != 1)
            return false;

        String subcommand = args[0];

        if(!subcommand.equalsIgnoreCase("disableparticles"))
            return false;

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messageDataProvider.getCommandPlayerOnly());
            return true;
        }

        List<String> disableParticlesList = seasonDataProvider.getPlayersDisabledParticles();

        if(!disableParticlesList.contains(player.getName())) {
            seasonDataProvider.disableParticlesForPlayer(player.getName());
            player.sendMessage(messageDataProvider.getCommandDisableParticlesOn());
        } else {
            seasonDataProvider.enableParticlesForPlayer(player.getName());
            player.sendMessage(messageDataProvider.getCommandDisableParticlesOff());
        }

        return true;
    }

    private boolean checkUpdate() {
        if(args.length != 1)
            return false;

        String arg = args[0];

        if(!arg.equalsIgnoreCase("update"))
            return false;

        PluginUpdater.getInstance().update(sender);
        return true;
    }

    private boolean checkReload() {
        if(args.length != 1)
            return false;

        String arg = args[0];

        if(!arg.equalsIgnoreCase("reload"))
            return false;

        PluginLogger.getInstance().clearLoggedMessages();
        seasonManager.reload();
        sender.sendMessage(messageDataProvider.getCommandReloadSuccess());

        if(configDataProvider.isCommandShowWarningsOnReloadEnabled()) {
            PluginLogger.getInstance().showLoggedMessages(sender);
        }

        return true;
    }

    private boolean checkSetSeason() {
        if(args.length != 2)
            return false;

        String arg0 = args[0];
        String newSeason = args[1];

        if(!arg0.equalsIgnoreCase("setseason"))
            return false;

        SeasonType seasonType = getSeasonType(newSeason);

        if(seasonType == null) {
            sender.sendMessage(messageDataProvider.getCommandInvalidSeasonInput());
            return true;
        }

        seasonManager.changeSeason(seasonType);
        sender.sendMessage(messageDataProvider.getCommandSetSeasonSuccess());
        return true;
    }

    private boolean checkSetRemainingTime() {
        if(args.length != 2)
            return false;

        String arg0 = args[0];
        String newRemainingTimeStr = args[1];
        int newRemainingTime;

        if(!arg0.equalsIgnoreCase("setremainingtime"))
            return false;

        try {
            newRemainingTime = Integer.parseInt(newRemainingTimeStr);

            if(newRemainingTime <= 0)
                throw new NumberFormatException();
        } catch(NumberFormatException e) {
            sender.sendMessage(messageDataProvider.getCommandInvalidNumberInput());
            return true;
        }

        seasonManager.setRemainingTime(newRemainingTime);
        sender.sendMessage(messageDataProvider.getCommandSetRemainingTimeSuccess());
        return true;
    }

    private boolean checkSpawnBoss() {
        if(args.length != 3)
            return false;

        String subcommand = args[0];
        String season = args[1];
        String bossName = args[2];

        if(!subcommand.equalsIgnoreCase("spawnboss"))
            return false;

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messageDataProvider.getCommandPlayerOnly());
            return true;
        }

        SeasonType seasonType = getSeasonType(season);

        if(seasonType == null) {
            player.sendMessage(messageDataProvider.getCommandInvalidSeasonInput());
            return true;
        }

        BossSpawningFeature feature = seasonManager.getSeason(seasonType).getBossSpawningFeature();
        List<String> bossList = feature.getAllEntryNames();

        if(!bossList.contains(bossName)) {
            player.sendMessage(messageDataProvider.getCommandSpawnBossUnknownBoss());
            return true;
        }

        feature.spawnBoss(player, bossName);
        player.sendMessage(messageDataProvider.getCommandSpawnBossSuccess(bossName));
        return true;
    }

    private boolean checkReport() {
        if(args.length < 3)
            return false;

        String subcommand = args[0];
        String rawType = args[1];
        StringBuilder msg = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        if(!subcommand.equalsIgnoreCase("report"))
            return false;

        if(feedbackHandler.isPrevented()) {
            sender.sendMessage(messageDataProvider.getCommandFeedbackCooldown());
            return true;
        }

        FeedbackType feedbackType;

        try {
            feedbackType = FeedbackType.valueOf(rawType.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messageDataProvider.getCommandFeedbackInvalidFeedbackType());
            return true;
        }

        Feedback feedback = new Feedback(feedbackType, msg.toString());

        sender.sendMessage(messageDataProvider.getCommandFeedbackSending());
        feedbackHandler.sendFeedback(feedback,
                                     () -> sender.sendMessage(messageDataProvider.getCommandFeedbackSuccess()),
                                     () -> sender.sendMessage(messageDataProvider.getCommandFeedbackFailure()));
        return true;
    }

    private SeasonType getSeasonType(String str) {
        return Arrays.stream(SeasonType.values()).filter(x -> x.name().equalsIgnoreCase(str.toUpperCase()))
                     .findFirst()
                     .orElse(null);
    }

    // /cmd <1> <2> ..
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!sender.hasPermission(configDataProvider.getCommandPermission()))
            return List.of();

        if(args.length == 1)
            return List.of("setseason", "setremainingtime", "reload", "update", "spawnboss", "report", "disableparticles");

        if(args.length == 2) {
            String arg = args[0];

            if(arg.equalsIgnoreCase("setseason") || arg.equalsIgnoreCase("spawnboss"))
                return Arrays.stream(SeasonType.values()).map(Enum::name).toList();

            if(arg.equalsIgnoreCase("setremainingtime"))
                sender.sendActionBar(Component.text(seasonManager.getRemainingTime()));

            if(arg.equalsIgnoreCase("report"))
                return Arrays.stream(FeedbackType.values()).map(Enum::name).toList();
        }

        if(args.length == 3) {
            String arg = args[0];
            SeasonType season = getSeasonType(args[1]);

            if(arg.equalsIgnoreCase("spawnboss")) {
                if (season == null)
                    return List.of();

                return seasonManager.getSeason(season).getBossSpawningFeature().getAllEntryNames();
            }
        }

        return List.of();
    }
}
