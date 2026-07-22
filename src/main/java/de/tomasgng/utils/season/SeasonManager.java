package de.tomasgng.utils.season;

import com.google.inject.Inject;
import de.tomasgng.DynamicSeasons;
import de.tomasgng.placeholders.IPlaceholderManager;
import de.tomasgng.utils.config.SeasonConfigManager;
import de.tomasgng.utils.config.dataproviders.ConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.MessageDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonConfigDataProvider;
import de.tomasgng.utils.config.dataproviders.SeasonDataProvider;
import de.tomasgng.utils.enums.SeasonType;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.List;

public class SeasonManager {

    private final DynamicSeasons plugin;
    private final BukkitAudiences adventure;
    private final SeasonDataProvider seasonDataProvider;
    private final ConfigDataProvider configDataProvider;
    private final SeasonConfigManager seasonConfigManager;
    private final IPlaceholderManager placeholderManager;
    private final MessageDataProvider messageDataProvider;

    private final List<Season> seasons;

    private Season currentSeason;
    private SeasonType lastSeasonType;
    private int remainingTime;

    @Inject
    public SeasonManager(DynamicSeasons plugin,
                         BukkitAudiences adventure,
                         SeasonDataProvider seasonDataProvider,
                         ConfigDataProvider configDataProvider,
                         SeasonConfigManager seasonConfigManager,
                         SeasonConfigDataProvider seasonConfigDataProvider,
                         IPlaceholderManager placeholderManager,
                         MessageDataProvider messageDataProvider) {
        this.plugin = plugin;
        this.adventure = adventure;
        this.seasonDataProvider = seasonDataProvider;
        this.configDataProvider = configDataProvider;
        this.seasonConfigManager = seasonConfigManager;
        this.placeholderManager = placeholderManager;
        this.messageDataProvider = messageDataProvider;

        seasons = List.of(
            new Season(SeasonType.SPRING, configDataProvider, seasonConfigDataProvider, seasonDataProvider, plugin),
            new Season(SeasonType.SUMMER, configDataProvider, seasonConfigDataProvider, seasonDataProvider, plugin),
            new Season(SeasonType.FALL, configDataProvider, seasonConfigDataProvider, seasonDataProvider, plugin),
            new Season(SeasonType.WINTER, configDataProvider, seasonConfigDataProvider, seasonDataProvider, plugin)
        );

        currentSeason = seasons.stream().filter(x -> x.getSeasonType() == seasonDataProvider.getCurrentSeason()).findFirst().get();
        remainingTime = seasonDataProvider.getRemainingDuration();
        seasonConfigManager.setConfigFile(currentSeason.getSeasonType());

        startSeasonTimer();
        currentSeason.init();
        initSeasonFeatures();
    }

    public void startSeasonTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin,
                                          this::decreaseRemainingTime,
                                          2 * 20L,
                                          20L);
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
        placeholderManager.reloadAll();
        currentSeason.init();
        initSeasonFeatures();
    }

    private void initSeasonFeatures() {
        seasons.forEach(x -> {
            if(x.getSeasonType() == currentSeason.getSeasonType())
                return;

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
        seasonConfigManager.setConfigFile(currentSeason.getSeasonType());
        currentSeason.init();

        announceSeasonChange();
    }

    private void announceSeasonChange() {
        if(configDataProvider.isSeasonChangeBroadcastEnabled()) {
            adventure.players().sendMessage(messageDataProvider.getSeasonChangeBroadcastMessage());
        }

        if(configDataProvider.isSeasonChangeTitleEnabled()) {
            Component mainTitle = messageDataProvider.getSeasonChangeTitle();
            Component subTitle = messageDataProvider.getSeasonChangeSubtitle();
            Title.Times times = Title.Times.times(Duration.ofSeconds(
                    configDataProvider.getSeasonChangeTitleFadeInDuration()),
                    Duration.ofSeconds(configDataProvider.getSeasonChangeTitleStayDuration()),
                    Duration.ofSeconds(configDataProvider.getSeasonChangeTitleFadeOutDuration()));

            Title title = Title.title(mainTitle, subTitle, times);

            adventure.players().showTitle(title);
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
