package de.tomasgng.utils.config.dataproviders;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.MessageManager;
import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.season.SeasonManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import static de.tomasgng.utils.config.pathproviders.MessagePathProvider.*;

public class MessageDataProvider {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final MessageManager manager;
    private SeasonManager seasonManager;
    private final ConfigDataProvider configDataProvider;

    public MessageDataProvider() {
        manager = DynamicSeasons.getInstance().getMessageManager();
        seasonManager = DynamicSeasons.getInstance().getSeasonManager();
        configDataProvider = DynamicSeasons.getInstance().getConfigDataProvider();
    }

    public Component getSeasonChangeBroadcastMessage() {
        return replaceAllPlaceholders(manager.getComponentValue(SEASON_CHANGE_BROADCAST));
    }

    public Component getSeasonChangeTitle() {
        return replaceAllPlaceholders(manager.getComponentValue(SEASON_CHANGE_TITLE));
    }

    public Component getSeasonChangeSubtitle() {
        return replaceAllPlaceholders(manager.getComponentValue(SEASON_CHANGE_SUBTITLE));
    }

    public Component getCommandUsage() {
        ConfigPair pair = COMMAND_USAGE;
        List<String> usageParts = manager.getStringListValue(pair).isEmpty() ? pair.getStringListValue() : manager.getStringListValue(pair);

        AtomicReference<Component> usageMsg = new AtomicReference<>(Component.text(""));

        for (int i = 0; i < usageParts.size(); i++) {
            String mmString = usageParts.get(i);

            try {
                Component partMsg = i == usageParts.size()-1
                        ? replaceAllPlaceholders(mmString)
                        : replaceAllPlaceholders(mmString + "\n");

                usageMsg.set(usageMsg.get().append(partMsg));
            } catch (Exception e) {
                DynamicSeasons.getInstance().getLogger().log(Level.WARNING, "The message {" + mmString + "} is not in MiniMessage format! Source (" + pair.getPath() + ")" + System.lineSeparator() + e.getMessage());
            }
        }

        return usageMsg.get();
    }

    public Component getCommandPlayerOnly() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_PLAYERONLY));
    }

    public Component getCommandNoPermission() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_NO_PERMISSION));
    }

    public Component getCommandInvalidSeasonInput() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_INVALID_SEASON_INPUT));
    }

    public Component getCommandInvalidNumberInput() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_INVALID_NUMBER_INPUT));
    }

    public Component getCommandSetSeasonSuccess() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_SET_SEASON_SUCCESS));
    }

    public Component getCommandSetRemainingTimeSuccess() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_SET_REMAINING_TIME_SUCCESS));
    }

    public Component getCommandReloadSuccess() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_RELOAD_SUCCESS));
    }

    public Component getCommandUpdateNoUpdatesAvailable() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_UPDATE_NO_UPDATES_AVAILABLE));
    }

    public Component getCommandUpdateStarted() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_UPDATE_STARTED));
    }

    public Component getCommandUpdateSuccess() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_UPDATE_SUCCESS));
    }

    public Component getCommandUpdateFailure() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_UPDATE_FAILURE));
    }

    public Component getCommandReloadWarnings() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_RELOAD_WARNINGS));
    }

    public Component getCommandSpawnBossUnknownBoss() {
        return replaceAllPlaceholders(manager.getComponentValue(COMMAND_SPAWNBOSS_UNKNOWBOSS));
    }

    public Component getCommandSpawnBossSuccess(String bossName) {
        Component msg = manager.getComponentValue(COMMAND_SPAWNBOSS_SUCCESS);
        String replacedBossPlaceholder = mm.serialize(msg).replaceAll("%boss%", bossName);
        msg = mm.deserialize(replacedBossPlaceholder);
        return replaceAllPlaceholders(msg);
    }

    private Component replaceAllPlaceholders(Component component) {
        String serialized = mm.serialize(component);

        return mm.deserialize(replaceAllPlaceholdersCore(serialized));
    }

    private Component replaceAllPlaceholders(String string) {
        return mm.deserialize(replaceAllPlaceholdersCore(string));
    }

    private String replaceAllPlaceholdersCore(String string) {
        if(seasonManager == null)
            seasonManager = DynamicSeasons.getInstance().getSeasonManager();

        string = string
                .replaceAll("%prefix%", manager.getStringValue(PREFIX))
                .replaceAll("%lastSeason%", configDataProvider.getCurrentSeasonReplacementText(seasonManager.getLastSeasonType()))
                .replaceAll("%newSeason%", configDataProvider.getCurrentSeasonReplacementText(seasonManager.getCurrentSeason().getSeasonType()))
                .replaceAll("%remainingTime%", "" + seasonManager.getRemainingTime());

        return string;
    }
}
