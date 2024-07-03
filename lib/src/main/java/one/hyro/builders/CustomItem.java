package one.hyro.builders;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import one.hyro.managers.MenuManager;
import one.hyro.utils.CustomHeads;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.function.Consumer;

@Getter
public class CustomItem {
    private String customId;
    private Material material;
    private ItemStack item;
    private ItemMeta meta;
    private Consumer<Player> consumer;

    public CustomItem(Material material) {
        this.material = material;
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public CustomItem(String texture) {
        this.item = CustomHeads.getCustomHead(texture);
        this.meta = (SkullMeta) item.getItemMeta();
    }

    public CustomItem(Player player) {
        this.item = CustomHeads.getPlayerHead(player);
        this.meta = (SkullMeta) item.getItemMeta();
    }

    public CustomItem setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    public CustomItem setDisplayName(Component component) {
        meta.displayName(component);
        return this;
    }

    public CustomItem setLore(Component components) {
        meta.lore(List.of(components));
        return this;
    }

    public CustomItem amount(int amount) {
        if (amount < 1) amount = 1;
        item.setAmount(amount);
        return this;
    }

    public CustomItem setEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        return this;
    }

    public CustomItem hideEnchantments() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public CustomItem onClick(Consumer<Player> consumer) {
        this.consumer = consumer;
        return this;
    }

    public CustomItem build() {
        item.setItemMeta(meta);
        MenuManager.getInstance().registerItem(this);
        return this;
    }
}
