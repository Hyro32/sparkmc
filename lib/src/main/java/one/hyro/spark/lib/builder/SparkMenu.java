package one.hyro.spark.lib.builder;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SparkMenu implements InventoryHolder {
    private final Inventory inventory;
    private final Map<Integer, SparkItem> items;
    @Setter private Component title;
    @Setter private int rows;

    public SparkMenu() {
        this.title = Component.text("Menu");
        this.rows = 3;
        this.items = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public SparkMenu setItem(int slot, SparkItem item) {
        items.put(slot, item);
        return this;
    }

    public SparkMenu fillColumn(int column, Material material) {
        for (int i = 0; i < rows; i++) {
            int slot = i * 9 + column;
            if (inventory.getItem(slot) != null) continue;
            SparkItem item = new SparkItem(material);
            setItem(i * 9 + column, item);
        }
        return this;
    }

    public SparkMenu fillRow(int row, Material material) {
        for (int i = 0; i < 9; i++) {
            int slot = row * 9 + i;
            if (inventory.getItem(slot) != null) continue;
            SparkItem item = new SparkItem(material);
            setItem(row * 9 + i, item);
        }
        return this;
    }

    public SparkMenu build() {
        items.forEach((slot, item) -> inventory.setItem(slot, item.getStack()));
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
