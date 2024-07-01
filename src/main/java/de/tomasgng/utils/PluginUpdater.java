package de.tomasgng.utils;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PluginUpdater {

    private static PluginUpdater instance;

    private final MessageDataProvider messageDataProvider = DynamicSeasons.getInstance().getMessageDataProvider();
    private final String downloadUrl = "https://api.spiget.org/v2/resources/111362/download";

    public void update(@Nullable CommandSender sender) {
        if(VersionChecker.getInstance().isLatestVersion(true)) {
            if(sender != null)
                sender.sendMessage(messageDataProvider.getCommandUpdateNoUpdatesAvailable());
            return;
        }

        if(sender != null)
            sender.sendMessage(messageDataProvider.getCommandUpdateStarted());

        String latestVersion = VersionChecker.getInstance().getUrlVersion();
        download(sender, latestVersion);
    }

    private void download(@Nullable CommandSender sender, String latestVersion) {
        Bukkit.getAsyncScheduler().runNow(DynamicSeasons.getInstance(), scheduledTask -> {
            if(!Bukkit.getUpdateFolderFile().exists())
                Bukkit.getUpdateFolderFile().mkdirs();

            File downloadFile = Path.of(Bukkit.getServer().getUpdateFolderFile().getPath(), "DynamicSeasons" + latestVersion.replaceAll("\\.", "_") + ".jar").toFile();

            try (InputStream in = new URI(downloadUrl).toURL().openStream()) {
                Files.copy(in, downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if(sender != null)
                    sendMessage(sender, messageDataProvider.getCommandUpdateSuccess());
            } catch (IOException | URISyntaxException e) {
                if(sender != null)
                    sendMessage(sender, messageDataProvider.getCommandUpdateFailure());

                PluginLogger.getInstance().error("Update failed: " + e);
            }
        });
    }

    private void sendMessage(CommandSender sender, Component msg) {
        Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), () -> sender.sendMessage(msg));
    }

    public static PluginUpdater getInstance() {
        if(instance == null)
            instance = new PluginUpdater();

        return instance;
    }
}
