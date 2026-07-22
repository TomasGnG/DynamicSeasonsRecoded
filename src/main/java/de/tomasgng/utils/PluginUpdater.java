package de.tomasgng.utils;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.interfaces.IPluginLogger;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PluginUpdater {

    private final DynamicSeasons plugin;
    private final IPluginLogger pluginLogger;
    private final MessageDataProvider messageDataProvider;
    private final BukkitAudiences adventure;
    private final VersionChecker versionChecker;
    private final String downloadUrl = "https://tomasgng.dev/plugins/dynamicseasons/download/DynamicSeasons.jar";

    public PluginUpdater(DynamicSeasons plugin,
                         IPluginLogger pluginLogger,
                         MessageDataProvider messageDataProvider,
                         BukkitAudiences adventure,
                         VersionChecker versionChecker) {
        this.plugin = plugin;
        this.pluginLogger = pluginLogger;
        this.messageDataProvider = messageDataProvider;
        this.adventure = adventure;
        this.versionChecker = versionChecker;
    }

    public void update(CommandSender sender) {
        if(versionChecker.isLatestVersion(true)) {
            if(sender != null)
                adventure.sender(sender).sendMessage(messageDataProvider.getCommandUpdateNoUpdatesAvailable());
            return;
        }

        if(sender != null)
            adventure.sender(sender).sendMessage(messageDataProvider.getCommandUpdateStarted());

        download(sender);
    }

    private void download(CommandSender sender) {
        Bukkit.getScheduler().runTask(plugin, scheduledTask -> {
            if(!Bukkit.getUpdateFolderFile().exists())
                Bukkit.getUpdateFolderFile().mkdirs();

            File downloadFile = Path.of(Bukkit.getServer().getUpdateFolderFile().getPath(), "DynamicSeasons.jar").toFile();

            try (InputStream in = new URI(downloadUrl).toURL().openStream()) {
                Files.copy(in, downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if(sender != null)
                    sendMessage(sender, messageDataProvider.getCommandUpdateSuccess());
            } catch (IOException | URISyntaxException e) {
                if(sender != null)
                    sendMessage(sender, messageDataProvider.getCommandUpdateFailure());

                pluginLogger.error("Update failed: " + e);
            }
        });
    }

    private void sendMessage(CommandSender sender, Component msg) {
        Bukkit.getScheduler().runTask(plugin, () -> adventure.sender(sender).sendMessage(msg));
    }
}
