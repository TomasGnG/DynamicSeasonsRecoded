package de.tomasgng.utils.config.dataproviders;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.tomasgng.utils.config.MessageManager;
import de.tomasgng.utils.config.utils.ConfigPair;
import de.tomasgng.utils.season.SeasonManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static de.tomasgng.utils.config.pathproviders.MessagePathProvider.*;

public class MessageDataProvider {

    private final MessageManager messageManager;
    private final Provider<SeasonManager> seasonManager;
    private final ConfigDataProvider configDataProvider;
    private final Logger logger;
    private final MiniMessage mm;

    @Inject
    public MessageDataProvider(MessageManager messageManager,
                               Provider<SeasonManager> seasonManager,
                               ConfigDataProvider configDataProvider,
                               Logger logger,
                               MiniMessage mm) {
        this.messageManager = messageManager;
        this.seasonManager = seasonManager;
        this.configDataProvider = configDataProvider;
        this.logger = logger;
        this.mm = mm;
    }

    public Component getSeasonChangeBroadcastMessage() {
        return replaceAllPlaceholders(messageManager.getComponentValue(SEASON_CHANGE_BROADCAST));
    }

    public Component getSeasonChangeTitle() {
        return replaceAllPlaceholders(messageManager.getComponentValue(SEASON_CHANGE_TITLE));
    }

    public Component getSeasonChangeSubtitle() {
        return replaceAllPlaceholders(messageManager.getComponentValue(SEASON_CHANGE_SUBTITLE));
    }

    public Component getCommandUsage() {
        ConfigPair pair = COMMAND_USAGE;
        List<String> usageParts = messageManager.getStringListValue(pair).isEmpty() ? pair.getStringListValue() : messageManager.getStringListValue(pair);

        AtomicReference<Component> usageMsg = new AtomicReference<>(Component.text(""));

        for (int i = 0; i < usageParts.size(); i++) {
            String mmString = usageParts.get(i);

            try {
                Component partMsg = i == usageParts.size()-1
                        ? replaceAllPlaceholders(mmString)
                        : replaceAllPlaceholders(mmString + "\n");

                usageMsg.set(usageMsg.get().append(partMsg));
            } catch (Exception e) {
                logger.warning("The message {" + mmString + "} is not in MiniMessage format! Source (" + pair.getPath() + ")" + System.lineSeparator() + e.getMessage());
            }
        }

        return usageMsg.get();
    }

    public Component getCommandPlayerOnly() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_PLAYERONLY));
    }

    public Component getCommandNoPermission() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_NO_PERMISSION));
    }

    public Component getCommandInvalidSeasonInput() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_INVALID_SEASON_INPUT));
    }

    public Component getCommandInvalidNumberInput() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_INVALID_NUMBER_INPUT));
    }

    public Component getCommandSetSeasonSuccess() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_SET_SEASON_SUCCESS));
    }

    public Component getCommandSetRemainingTimeSuccess() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_SET_REMAINING_TIME_SUCCESS));
    }

    public Component getCommandReloadSuccess() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_RELOAD_SUCCESS));
    }

    public Component getCommandUpdateNoUpdatesAvailable() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_UPDATE_NO_UPDATES_AVAILABLE));
    }

    public Component getCommandUpdateStarted() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_UPDATE_STARTED));
    }

    public Component getCommandUpdateSuccess() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_UPDATE_SUCCESS));
    }

    public Component getCommandUpdateFailure() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_UPDATE_FAILURE));
    }

    public Component getCommandReloadWarnings() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_RELOAD_WARNINGS));
    }

    public Component getCommandSpawnBossUnknownBoss() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_SPAWNBOSS_UNKNOWBOSS));
    }

    public Component getCommandSpawnBossSuccess(String bossName) {
        Component msg = messageManager.getComponentValue(COMMAND_SPAWNBOSS_SUCCESS);
        String replacedBossPlaceholder = mm.serialize(msg).replaceAll("%boss%", bossName);
        msg = mm.deserialize(replacedBossPlaceholder);
        return replaceAllPlaceholders(msg);
    }

    public Component getCommandFeedbackCooldown() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_FEEDBACK_COOLDOWN));
    }

    public Component getCommandFeedbackInvalidFeedbackType() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_FEEDBACK_INVALID_FEEDBACK_TYPE));
    }

    public Component getCommandFeedbackSending() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_FEEDBACK_SENDING));
    }

    public Component getCommandFeedbackSuccess() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_FEEDBACK_SUCCESS));
    }

    public Component getCommandFeedbackFailure() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_FEEDBACK_FAILURE));
    }

    public Component getCommandDisableParticlesOn() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_DISABLEPARTICLES_ON));
    }

    public Component getCommandDisableParticlesOff() {
        return replaceAllPlaceholders(messageManager.getComponentValue(COMMAND_DISABLEPARTICLES_OFF));
    }

    private Component replaceAllPlaceholders(Component component) {
        String serialized = mm.serialize(component);

        return mm.deserialize(replaceAllPlaceholdersCore(serialized));
    }

    private Component replaceAllPlaceholders(String string) {
        return mm.deserialize(replaceAllPlaceholdersCore(string));
    }

    private String replaceAllPlaceholdersCore(String string) {
        SeasonManager seasonManager = this.seasonManager.get();

        string = string
                .replaceAll("%prefix%", messageManager.getStringValue(PREFIX))
                .replaceAll("%lastSeason%", configDataProvider.getCurrentSeasonReplacementText(seasonManager.getLastSeasonType()))
                .replaceAll("%newSeason%", configDataProvider.getCurrentSeasonReplacementText(seasonManager.getCurrentSeason().getSeasonType()))
                .replaceAll("%remainingTime%", "" + seasonManager.getRemainingTime());

        return string;
    }
}
