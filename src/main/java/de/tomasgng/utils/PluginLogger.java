package de.tomasgng.utils;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PluginLogger {

    private static final PluginLogger pluginLogger = new PluginLogger();

    private final List<String> loggedMessages = new ArrayList<>();

    public void warn(String message) {
        loggedMessages.add(message);
        DynamicSeasons.getInstance().getLogger().warning(message);
    }

    public void error(String message) {
        loggedMessages.add(message);
        DynamicSeasons.getInstance().getLogger().severe(message);
    }

    public void showLoggedMessages(CommandSender sender) {
        if(loggedMessages.isEmpty())
            return;

        MessageDataProvider messageDataProvider = DynamicSeasons.getInstance().getMessageDataProvider();

        sender.sendMessage(messageDataProvider.getCommandReloadWarnings());

        for (int i = 0; i < loggedMessages.size(); i++) {
            sender.sendMessage(Component.text((i + 1) + ". ")
                                        .color(NamedTextColor.YELLOW)
                                        .append(Component.text(loggedMessages.get(i).replaceAll("§", "&"))));
        }

        sender.sendMessage("");
    }

    public void clearLoggedMessages() {
        loggedMessages.clear();
    }

    public static PluginLogger getInstance() {
        return pluginLogger;
    }
}
