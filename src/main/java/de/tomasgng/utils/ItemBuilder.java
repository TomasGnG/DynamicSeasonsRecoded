package de.tomasgng.utils;

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private final LegacyComponentSerializer legacy = BukkitComponentSerializer.legacy();

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    public ItemBuilder displayname(Component displayname) {
        this.itemMeta.setDisplayName(legacy.serialize(displayname.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        return this;
    }

    public ItemBuilder lore(Component... lore) {
        List<Component> formattedLore = Arrays.stream(lore)
                                              .map(x -> x.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                              .toList();

        this.itemMeta.setLore(formattedLore.stream().map(legacy::serialize).toList());
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        List<Component> formattedLore = lore
                .stream()
                .map(x -> x.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();

        this.itemMeta.setLore(formattedLore.stream().map(legacy::serialize).toList());
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach((key, value) -> this.itemMeta.addEnchant(key, value, true));
        return this;
    }
}
