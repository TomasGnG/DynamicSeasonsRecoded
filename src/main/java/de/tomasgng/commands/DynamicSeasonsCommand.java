package de.tomasgng.commands;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.PluginLogger;
import de.tomasgng.utils.PluginUpdater;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import de.tomasgng.utils.season.SeasonManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DynamicSeasonsCommand extends Command {

    private final ConfigDataProvider configDataProvider;
    private final MessageDataProvider messageDataProvider;
    private final SeasonManager seasonManager;

    private CommandSender sender;
    private String[] args;

    public DynamicSeasonsCommand() {
        super(DynamicSeasons.getInstance().getConfigDataProvider().getCommandName(),
                DynamicSeasons.getInstance().getConfigDataProvider().getCommandDescription(),
                "",
                DynamicSeasons.getInstance().getConfigDataProvider().getCommandAliases());

        configDataProvider = DynamicSeasons.getInstance().getConfigDataProvider();
        messageDataProvider = DynamicSeasons.getInstance().getMessageDataProvider();
        seasonManager = DynamicSeasons.getInstance().getSeasonManager();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(!sender.hasPermission(configDataProvider.getCommandPermission())) {
            sender.sendMessage(messageDataProvider.getCommandNoPermission());
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage(messageDataProvider.getCommandUsage());
            return false;
        }

        this.sender = sender;
        this.args = args;

        if(checkReload())
            return false;

        if(checkUpdate())
            return false;

        if(checkSetSeason())
            return false;

        if(checkSetRemainingTime())
            return false;

        sender.sendMessage(messageDataProvider.getCommandUsage());
        return false;
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

        SeasonType seasonType = Arrays.stream(SeasonType.values()).filter(x -> x.name().equalsIgnoreCase(newSeason))
                .findFirst()
                .orElse(null);

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

    // /cmd <1> <2> ..
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!sender.hasPermission(configDataProvider.getCommandPermission()))
            return List.of();

        if(args.length == 1)
            return List.of("setseason", "setremainingtime", "reload", "update");

        if(args.length == 2) {
            String arg = args[0];

            if(arg.equalsIgnoreCase("setseason"))
                return Arrays.stream(SeasonType.values()).map(Enum::name).toList();

            if(arg.equalsIgnoreCase("setremainingtime"))
                sender.sendActionBar(Component.text(seasonManager.getRemainingTime()));
        }

        return List.of();
    }
}
