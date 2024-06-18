package de.tomasgng;

import de.tomasgng.utils.VersionChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class DynamicSeasons extends JavaPlugin {

    private static DynamicSeasons INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        VersionChecker.getInstance().check();
    }

    @Override
    public void onDisable() {

    }

    public static DynamicSeasons getInstance() {
        return INSTANCE;
    }
}
