package one.hyro.builders;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import one.hyro.managers.MenuManager;
import one.hyro.utils.CustomHeads;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
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
    }

    public CustomItem(Player player) {
        this.item = CustomHeads.getPlayerHead(player);
    }

    public CustomItem setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    public CustomItem setDisplayName(Component component) {
        meta.displayName(component);
        return this;
    }

    public CustomItem setLore(List<Component> components) {
        meta.lore(components);
        return this;
    }

    public CustomItem amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public CustomItem enchanted() {
        meta.addEnchant(Enchantment.DENSITY, 1, false);
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
