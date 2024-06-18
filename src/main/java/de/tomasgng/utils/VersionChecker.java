package de.tomasgng.utils;

import de.tomasgng.DynamicSeasons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public final class VersionChecker {

    private static VersionChecker INSTANCE = new VersionChecker();

    private Logger logger = DynamicSeasons.getInstance().getLogger();
    private MiniMessage mm = MiniMessage.miniMessage();

    private String versionUrl = "https://pastebin.com/raw/DZXYPzR7";
    private String currentVersion = DynamicSeasons.getInstance().getPluginMeta().getVersion();


    public static VersionChecker getInstance() {
        return INSTANCE;
    }

    public void check() {
        String urlVersion = getUrlVersion();

        List<Integer> urlVersionSplitted = Arrays.stream(urlVersion.split("\\.")).map(Integer::parseInt).toList();
        List<Integer> currentVersionSplitted = Arrays.stream(currentVersion.split("\\.")).map(Integer::parseInt).toList();

        boolean isUsingLatestVersion = true;
        for (int i = 0; i < currentVersionSplitted.size(); i++) {
            int currentInteger =  currentVersionSplitted.get(i);
            int urlInteger = i >= urlVersionSplitted.size() ? 0 : urlVersionSplitted.get(i);

            if(currentInteger > urlInteger)
                break;

            if(currentInteger < urlInteger) {
                System.out.println(currentInteger + " | " + urlInteger);
                isUsingLatestVersion = false;
                break;
            }
        }

        for (int i = 0; i < urlVersionSplitted.size(); i++) {
            int urlInteger =  urlVersionSplitted.get(i);
            int currentInteger = i >= currentVersionSplitted.size() ? 0 : currentVersionSplitted.get(i);

            if(currentInteger > urlInteger)
                break;

            if(currentInteger < urlInteger) {
                System.out.println(currentInteger + " | " + urlInteger);
                isUsingLatestVersion = false;
                break;
            }
        }

        if(!isUsingLatestVersion) {
            sendConsoleMessage(mm.deserialize("<gradient:#55C156:#FFFF00:#FFA500:#87CEFA>DynamicSeasons</gradient> <dark_gray>| <yellow>Using an outdated version(" + currentVersion + "). Newest version " + urlVersion));
            sendConsoleMessage(mm.deserialize("<gradient:#55C156:#FFFF00:#FFA500:#87CEFA>DynamicSeasons</gradient> <dark_gray>| <yellow>Download: https://www.spigotmc.org/resources/dynamicseasons-%E2%8C%9B-enhance-your-survival-experience-%E2%9C%85.111362/"));
            return;
        }

        sendConsoleMessage(mm.deserialize("<gradient:#55C156:#FFFF00:#FFA500:#87CEFA>DynamicSeasons</gradient> <dark_gray>| <green>Using the latest version(" + currentVersion + ")."));
        sendConsoleMessage(mm.deserialize("<gradient:#55C156:#FFFF00:#FFA500:#87CEFA>DynamicSeasons</gradient> <dark_gray>| <green>Thank you for using my plugin ;)"));
    }

    private String getUrlVersion() {
        String version = currentVersion;

        try {
            URL url = new URI(versionUrl).toURL();
            Scanner scanner = new Scanner(url.openStream());

            if(scanner.hasNext())
                version = scanner.next();
        } catch (URISyntaxException | IOException e) {
            logger.severe(e.getLocalizedMessage());
        }

        return version;
    }

    private void sendConsoleMessage(Component component) {
        Bukkit.getConsoleSender().sendMessage(component);
    }

}
