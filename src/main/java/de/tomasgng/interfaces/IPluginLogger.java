package de.tomasgng.interfaces;

import org.bukkit.command.CommandSender;

public interface IPluginLogger {
    void warn(String message);
    void error(String message);
    void showLoggedMessages(CommandSender sender);
    void clearLoggedMessages();
}
