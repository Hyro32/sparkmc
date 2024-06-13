package one.hyro.paper.managers;

import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PersistentDataKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenusManager {
    private static final Map<String, Inventory> menus = new HashMap<>();

    public static void loadMenus() {
        File dataFolder = new File(HyrosPaper.getInstance().getDataFolder(), "menus");
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            Inventory menu = createMenuFromConfig(config);
            menus.put(file.getName(), menu);
            System.out.println("Loaded menu: " + file.getName());
        }
    }

    private static Inventory createMenuFromConfig(FileConfiguration config) {
        String title = config.getString("title", "Menu");
        int size = config.getInt("slots", 9 * 3);
        Inventory menu = Bukkit.createInventory(null, size, title);

        List<Map<?, ?>> itemsList = config.getMapList("items");

        for (Map<?, ?> itemMap : itemsList) {
            String materialName = (String) itemMap.get("material");
            Material material = Material.getMaterial(materialName);
            if (material == null) continue;

            int slot = (int) itemMap.get("slot");
            boolean enchanted = (boolean) itemMap.get("enchanted");
            String name = (String) itemMap.get("name");
            List<String> lore = (List<String>) itemMap.get("lore");

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(name);
            meta.setLore(lore);

            if (enchanted) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DENSITY, 1, false);
            }

            item.setItemMeta(meta);
            menu.setItem(slot, item);
        }

        return menu;
    }

    public static void openMenu(Player player, String menuName) {
        Inventory menu = menus.get(menuName + ".yml");

        if (menu == null) {
            player.sendMessage("Invalid menu name.");
            return;
        }

        NamespacedKey key = new NamespacedKey(
                HyrosPaper.getInstance(),
                PersistentDataKeys.CUSTOM_MENU.getKey()
        );

        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        player.openInventory(menu);
    }
}
