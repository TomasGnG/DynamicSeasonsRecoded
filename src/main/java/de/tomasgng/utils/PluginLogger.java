package de.tomasgng.utils;

import com.google.inject.Inject;
import de.tomasgng.interfaces.IPluginLogger;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PluginLogger implements IPluginLogger {
    
    private final List<String> loggedMessages = new ArrayList<>();
    private final MessageDataProvider messageDataProvider;
    private final BukkitAudiences adventure;
    private final Logger logger;

    @Inject
    public PluginLogger(MessageDataProvider messageDataProvider, BukkitAudiences adventure, Logger logger) {
        this.messageDataProvider = messageDataProvider;
        this.adventure = adventure;
        this.logger = logger;
    }

    public void warn(String message) {
        loggedMessages.add(message);
        logger.warning(message);
    }

    public void error(String message) {
        loggedMessages.add(message);
        logger.severe(message);
    }

    public void showLoggedMessages(CommandSender sender) {
        if(loggedMessages.isEmpty())
            return;

        adventure.sender(sender).sendMessage(messageDataProvider.getCommandReloadWarnings());

        for (int i = 0; i < loggedMessages.size(); i++) {
            adventure.sender(sender).sendMessage(Component.text((i + 1) + ". ")
                                        .color(NamedTextColor.YELLOW)
                                        .append(Component.text(loggedMessages.get(i).replaceAll("§", "&"))));
        }

        sender.sendMessage("");
    }

    public void clearLoggedMessages() {
        loggedMessages.clear();
    }
}
