package de.tomasgng.utils.features.utils;

import de.tomasgng.DynamicSeasons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public final class BossSpawningEntry {

    public BossSpawningEntry(String entryName,
                             boolean enabled,
                             EntityType mobType,
                             Component displayname,
                             int spawnChance,
                             BossSpawningItemInHandEntry itemInHand,
                             Map<Attribute, Double> attributes,
                             boolean lootDropsEnabled,
                             List<LootDropsEntry> lootDrops) {
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        this.entryName = entryName;
        this.enabled = enabled;
        this.mobType = mobType;
        this.displayname = displayname;
        this.spawnChance = spawnChance;
        this.itemInHand = itemInHand;
        this.attributes = attributes;
        this.lootDropsEnabled = lootDropsEnabled;
        this.lootDrops = lootDrops;
    }

    public BossSpawningEntry(BossSpawningEntry other) {
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        this.entryName = other.entryName;
        this.enabled = other.enabled;
        this.mobType = other.mobType;
        this.displayname = other.displayname;
        this.spawnChance = other.spawnChance;
        this.itemInHand = other.itemInHand;
        this.attributes = other.attributes;
        this.lootDropsEnabled = other.lootDropsEnabled;
        this.lootDrops = other.lootDrops;
    }

    private static final Random random = new Random();
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final DecimalFormat df = new DecimalFormat("#");

    private final String entryName;
    private final boolean enabled;
    private final EntityType mobType;
    private final Component displayname;
    private final int spawnChance;
    private final BossSpawningItemInHandEntry itemInHand;
    private final Map<Attribute, Double> attributes;
    private final boolean lootDropsEnabled;
    private final List<LootDropsEntry> lootDrops;

    public LivingEntity entity;

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
        Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), this::applyCustomizations);
    }

    public void applyCustomizations() {
        entity.setPersistent(true);

        attributes.forEach((attribute, value) -> {
            if (entity.getAttribute(attribute) != null)
                entity.getAttribute(attribute).setBaseValue(attribute == Attribute.GENERIC_MAX_HEALTH ? value*2 : value);

            if (attribute == Attribute.GENERIC_MAX_HEALTH)
                entity.setHealth(value*2);
        });

        if (itemInHand != null && itemInHand.isEnabled() && entity.getEquipment() != null) {
            entity.getEquipment().setItemInMainHand(itemInHand.item());
            entity.getEquipment().setItemInMainHandDropChance(itemInHand.dropChance() / 100f);
        }

        if (displayname != null)
            applyDisplayname();
    }

    public void applyDisplayname() {
        if (displayname == null)
            return;

        Bukkit.getScheduler().runTask(DynamicSeasons.getInstance(), () -> {
            String health = df.format(entity.getHealth() > 0 ? entity.getHealth() / 2 : 0);
            String maxHealth = df.format(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2);

            String replaced = mm.serialize(displayname)
                                .replaceAll("%health%", health)
                                .replace("%maxhealth%", maxHealth);

            entity.customName(mm.deserialize(replaced));
        });
    }

    public List<ItemStack> getLootDrops() {
        List<ItemStack> drops = new ArrayList<>();

        if (!lootDropsEnabled)
            return drops;

        int randomSpawnChance = random.nextInt(101);

        for (LootDropsEntry entry : lootDrops) {
            if (randomSpawnChance <= entry.dropChance())
                drops.add(entry.itemStack());
        }

        return drops;
    }

    public boolean shouldSpawnAsBoss(int randomNumber) {
        return randomNumber <= spawnChance;
    }

    public boolean isCorrectMobType(EntityType other) {
        return mobType == other;
    }

    public int getSpawnChance() {
        return spawnChance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getEntryName() {
        return entryName;
    }

    public EntityType getMobType() {
        return mobType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BossSpawningEntry) obj;
        return Objects.equals(this.mobType, that.mobType) &&
                Objects.equals(this.displayname, that.displayname) &&
                this.spawnChance == that.spawnChance &&
                Objects.equals(this.itemInHand, that.itemInHand) &&
                Objects.equals(this.attributes, that.attributes) &&
                this.lootDropsEnabled == that.lootDropsEnabled &&
                Objects.equals(this.lootDrops, that.lootDrops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobType, displayname, spawnChance, itemInHand, attributes, lootDropsEnabled, lootDrops);
    }

    @Override
    public String toString() {
        return "BossSpawningEntry[" +
                "mobType=" + mobType + ", " +
                "displayname=" + displayname + ", " +
                "spawnChance=" + spawnChance + ", " +
                "itemInHand=" + itemInHand + ", " +
                "attributes=" + attributes + ", " +
                "lootDropsEnabled=" + lootDropsEnabled + ", " +
                "lootDrops=" + lootDrops + ']';
    }

}
