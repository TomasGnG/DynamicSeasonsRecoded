package de.tomasgng.utils.season;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.config.SeasonConfigManager;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeasonManager {

    private final SeasonDataProvider seasonDataProvider;
    private final ConfigDataProvider configDataProvider;
    private final MessageDataProvider messageDataProvider;
    private final SeasonConfigManager seasonConfigManager;

    private final List<Season> seasons;
    {
        seasons = List.of(new Season(SeasonType.SPRING),
                          new Season(SeasonType.SUMMER),
                          new Season(SeasonType.FALL),
                          new Season(SeasonType.WINTER));
    }

    private Season currentSeason;
    private SeasonType lastSeasonType;
    private int remainingTime;

    public SeasonManager() {
        seasonDataProvider = DynamicSeasons.getInstance().getSeasonDataProvider();
        configDataProvider = DynamicSeasons.getInstance().getConfigDataProvider();
        messageDataProvider = DynamicSeasons.getInstance().getMessageDataProvider();
        seasonConfigManager = DynamicSeasons.getInstance().getSeasonConfigManager();

        currentSeason = seasons.stream().filter(x -> x.getSeasonType() == seasonDataProvider.getCurrentSeason()).findFirst().get();
        remainingTime = seasonDataProvider.getRemainingDuration();

        seasonConfigManager.setConfigFile(currentSeason.getSeasonType());

        startSeasonTimer();
        currentSeason.init();
        initSeasonFeatures();
    }

    public void startSeasonTimer() {
        Bukkit.getAsyncScheduler().runAtFixedRate(DynamicSeasons.getInstance(), task -> {
            decreaseRemainingTime();
        }, 2, 1, TimeUnit.SECONDS);
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    private void decreaseRemainingTime() {
        if(remainingTime - 1 < 0) {
            remainingTime = configDataProvider.getSeasonDuration();
            changeSeason();
            return;
        }

        remainingTime--;
        seasonDataProvider.setRemainingDuration(remainingTime);
    }

    public void changeSeason(SeasonType newSeasonType) {
        currentSeason.stopAllTimers();
        lastSeasonType = currentSeason.getSeasonType();
        currentSeason = seasons.stream().filter(x -> x.getSeasonType() == newSeasonType).findFirst().get();
        seasonDataProvider.setCurrentSeason(currentSeason.getSeasonType());
        seasonDataProvider.setRemainingDuration(configDataProvider.getSeasonDuration());
        remainingTime = seasonDataProvider.getRemainingDuration();
        seasonConfigManager.setConfigFile(currentSeason.getSeasonType());
        currentSeason.init();

        announceSeasonChange();
    }

    public void reload() {
        seasonConfigManager.createFiles();
        DynamicSeasons.getInstance().getPlaceholderManager().reloadAll();
        currentSeason.init();
        initSeasonFeatures();
    }

    private void initSeasonFeatures() {
        seasons.forEach(x -> {
            seasonConfigManager.setConfigFile(x.getSeasonType());
            x.initFeatures();
        });

        seasonConfigManager.setConfigFile(currentSeason.getSeasonType());
    }

    private void changeSeason() {
        currentSeason.stopAllTimers();
        lastSeasonType = currentSeason.getSeasonType();
        int seasonIndex = seasons.indexOf(currentSeason);
        currentSeason = seasons.get((seasonIndex+1) % seasons.size());
        seasonDataProvider.setCurrentSeason(currentSeason.getSeasonType());
        seasonDataProvider.setRemainingDuration(configDataProvider.getSeasonDuration());
        remainingTime = seasonDataProvider.getRemainingDuration();
        DynamicSeasons.getInstance().getSeasonConfigManager().setConfigFile(currentSeason.getSeasonType());
        currentSeason.init();

        announceSeasonChange();
    }

    private void announceSeasonChange() {
        if(configDataProvider.isSeasonChangeBroadcastEnabled()) {
            Bukkit.broadcast(messageDataProvider.getSeasonChangeBroadcastMessage());
        }

        if(configDataProvider.isSeasonChangeTitleEnabled()) {
            Component mainTitle = messageDataProvider.getSeasonChangeTitle();
            Component subTitle = messageDataProvider.getSeasonChangeSubtitle();
            Title.Times times = Title.Times.times(Duration.ofSeconds(
                    configDataProvider.getSeasonChangeTitleFadeInDuration()),
                    Duration.ofSeconds(configDataProvider.getSeasonChangeTitleStayDuration()),
                    Duration.ofSeconds(configDataProvider.getSeasonChangeTitleFadeOutDuration()));

            Title title = Title.title(mainTitle, subTitle, times);

            Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(title));
        }
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Season getSeason(SeasonType type) {
        return seasons.stream().filter(x -> x.getSeasonType() == type).findFirst().orElse(null);
    }

    public SeasonType getLastSeasonType() {
        if(lastSeasonType == null)
            lastSeasonType = seasons.get(Math.floorMod((seasons.indexOf(currentSeason)-1), seasons.size())).getSeasonType();

        return lastSeasonType;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
