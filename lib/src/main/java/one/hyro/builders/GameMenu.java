package one.hyro.builders;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import one.hyro.managers.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GameMenu {
    private String customId;
    private InventoryHolder holder;
    private int size;
    private Component title;
    private Inventory inventory;
    private final Map<Integer, CustomItem> items;

    public GameMenu() {
        this.title = Component.text("Menu");
        this.size = 27;
        this.holder = null;
        this.items = new HashMap<>();
    }

    public GameMenu setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    public GameMenu setHolder(@Nullable InventoryHolder holder) {
        this.holder = holder;
        return this;
    }

    public GameMenu setSize(int size) {
        this.size = size;
        return this;
    }

    public GameMenu setTitle(Component title) {
        this.title = title;
        return this;
    }

    public GameMenu setItem(int slot, CustomItem customItem) {
        if (inventory != null && inventory.getItem(slot) != null) return this;
        items.put(slot, customItem);
        return this;
    }

    public GameMenu fill(Material material) {
        for (int i = 0; i < size; i++) {
            if (inventory != null && inventory.getItem(i) != null) continue;
            CustomItem item = new CustomItem(material)
                    .setDisplayName(Component.text(""))
                    .build();
            items.put(i, item);
        }
        return this;
    }

    public GameMenu fillRow(int row, Material material) {
        for (int i = row * 9; i < (row + 1) * 9; i++) {
            if (inventory != null && inventory.getItem(i) != null) continue;
            CustomItem item = new CustomItem(material)
                    .setDisplayName(Component.text(""))
                    .build();
            items.put(i, item);
        }
        return this;
    }

    public GameMenu build() {
        if (customId == null) {
            throw new IllegalArgumentException("Custom ID cannot be null");
        }

        inventory = Bukkit.createInventory(holder, size, title);
        items.forEach((slot, customItem) -> inventory.setItem(slot, customItem.getItem()));
        MenuManager.getInstance().registerMenu(this);
        return this;
    }
}