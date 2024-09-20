package de.tomasgng.utils.features;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.features.utils.CommandExecutionEntry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public record CommandExecutionFeature(boolean enabled, List<String> onSeasonChangePlayerCommands, List<String> onSeasonChangeConsoleCommands, List<CommandExecutionEntry> runAfterEntries) {

    private static Random random = new Random();

    public void initialize() {
        if (!enabled)
            return;

        executeCommands();
        startRunAfterEntries();
    }

    private void executeCommands() {
        for (String cmd : onSeasonChangePlayerCommands) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                cmd = cmd.replace("%player%", player.getName());
                player.performCommand(cmd);
            }
        }

        for (String cmd : onSeasonChangeConsoleCommands) {
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

    private void startRunAfterEntries() {
        List<CommandExecutionEntry> enabledEntries = runAfterEntries.stream().filter(CommandExecutionEntry::enabled).toList();

        for (CommandExecutionEntry entry : enabledEntries) {
            int generatedNumber = random.nextInt(entry.runAfterMin(), entry.runAfterMax()+1);

            Bukkit.getScheduler().runTaskLaterAsynchronously(DynamicSeasons.getInstance(), () -> {
                Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), entry::executeCommands);
            }, generatedNumber * 20L);
        }
    }
}
