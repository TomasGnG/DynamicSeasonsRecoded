package de.tomasgng.utils.features.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public record CommandExecutionEntry(boolean enabled, int runAfterMin, int runAfterMax, List<String> playerCommands, List<String> consoleCommands) {

    public void executeCommands() {
        for (String cmd : playerCommands) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                cmd = cmd.replace("%player%", player.getName());
                player.performCommand(cmd);
            }
        }

        for (String cmd : consoleCommands) {
            if(cmd.contains("%player%")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    cmd = cmd.replace("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }

                continue;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

}
