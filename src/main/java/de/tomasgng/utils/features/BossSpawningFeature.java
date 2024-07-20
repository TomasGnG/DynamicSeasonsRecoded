package de.tomasgng.utils.features;

import de.tomasgng.DynamicSeasons;
import de.tomasgng.utils.features.utils.BossSpawningEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record BossSpawningFeature(boolean isEnabled, List<BossSpawningEntry> entries) {
    private static final List<BossSpawningEntry> livingBosses = new ArrayList<>();
    public static final NamespacedKey MANUAL_SPAWN_KEY = new NamespacedKey(DynamicSeasons.getInstance(), "spawnAsBoss");

    public void addLivingBoss(BossSpawningEntry entry) {
        livingBosses.add(entry);
    }

    public BossSpawningEntry getLivingBossEntry(LivingEntity entity) {
        return livingBosses.stream().filter(x -> x.entity.equals(entity)).findFirst().orElse(null);
    }

    public void removeLivingBoss(LivingEntity entity) {
        livingBosses.removeIf(x -> x.entity.equals(entity));
    }

    public boolean isLivingBoss(LivingEntity entity) {
        return livingBosses.stream().anyMatch(x -> x.entity.equals(entity));
    }

    public List<BossSpawningEntry> getEntries(EntityType type) {
        List<BossSpawningEntry> foundEntries = new ArrayList<>(entries.stream().filter(x -> x.isCorrectMobType(type) && x.isEnabled()).toList());
        foundEntries.sort(Comparator.comparing(BossSpawningEntry::getSpawnChance));
        return foundEntries;
    }

    public BossSpawningEntry getEntryByName(String bossName) {
        return entries.stream().filter(x -> x.getEntryName().equals(bossName)).findFirst().orElse(null);
    }

    public void spawnBoss(Player player, String bossName) {
        BossSpawningEntry found = getEntryByName(bossName);

        if (found == null)
            return;

        BossSpawningEntry entry = new BossSpawningEntry(found);

        var entity = player.getWorld().spawnEntity(player.getLocation(), entry.getMobType());
        entity.getPersistentDataContainer().set(MANUAL_SPAWN_KEY, PersistentDataType.STRING, entry.getEntryName());
        entry.setEntity((LivingEntity) entity);
        addLivingBoss(entry);
    }

    public List<String> getAllEntryNames() {
        return entries.stream()
                      .map(BossSpawningEntry::getEntryName)
                      .toList();
    }
}
