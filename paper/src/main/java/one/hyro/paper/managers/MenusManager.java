package one.hyro.paper.managers;

import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PersistentDataKeys;
import one.hyro.paper.utilities.Chalk;
import one.hyro.paper.utilities.ConfigParser;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MenusManager {
    private static final Map<String, FileConfiguration> menus = new HashMap<>();
    private static final NamespacedKey key = PersistentDataKeys.CUSTOM_MENU.getKey();

    public static void loadMenus() {
        File dataFolder = new File(HyrosPaper.getInstance().getDataFolder(), "menus");
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            menus.put(file.getName(), config);
            Bukkit.getLogger().info("Loaded menu: " + file.getName());
        }
    }

    public static void openMenu(Player player, String menuName) {
        FileConfiguration config = menus.get(menuName + ".yml");

        if (config == null) {
            player.sendMessage("Invalid menu name.");
            return;
        }

        Inventory menu = createMenuFromConfig(config, player);
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, menuName);
        player.openInventory(menu);
    }

    private static Inventory createMenuFromConfig(FileConfiguration config, Player player) {
        String title = config.getString("title", "Menu");
        int size = config.getInt("slots", 9 * 3);
        Inventory menu = Bukkit.createInventory(null, size, Chalk.colorizeLegacy(title));
        Map<ItemStack, Integer> items = ConfigParser.parseItems(config, "items", player);

        for (Map.Entry<ItemStack, Integer> entry : items.entrySet()) {
            ItemStack item = entry.getKey();
            int slot = entry.getValue();
            menu.setItem(slot, item);
        }

        return menu;
    }
}