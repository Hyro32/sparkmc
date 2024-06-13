package one.hyro.paper.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PersistentDataKeys;
import one.hyro.paper.utilities.Chalk;
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

    public static void runItemCommands(int slot, String menuName, Player player) {
        FileConfiguration menuConfig = menus.get(menuName + ".yml");
        List<Map<?, ?>> itemsList = menuConfig.getMapList("items");

        for (Map<?, ?> itemMap : itemsList) {
            if ((int) itemMap.get("slot") != slot) continue;
            List<String> commands = (List<String>) itemMap.get("commands");

            for (String command : commands) {
                String label = command.split(" ")[0];
                String args = command.substring(label.length() + 1);

                switch (label) {
                    case "connect":
                        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(args);
                        player.sendPluginMessage(HyrosPaper.getInstance(), "BungeeCord", out.toByteArray());
                        break;
                    case "menu":
                        openMenu(player, args);
                        break;
                    case "message":
                        player.sendMessage(args);
                        break;
                    default:
                        Bukkit.getLogger().warning("Invalid command type: " + label);
                }
            }
        }
    }

    public static void openMenu(Player player, String menuName) {
        FileConfiguration config = menus.get(menuName + ".yml");

        if (config == null) {
            player.sendMessage("Invalid menu name.");
            return;
        }

        Inventory menu = createMenuFromConfig(config);
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, menuName);
        player.openInventory(menu);
    }

    private static Inventory createMenuFromConfig(FileConfiguration config) {
        String title = config.getString("title", "Menu");
        int size = config.getInt("slots", 9 * 3);
        Inventory menu = Bukkit.createInventory(null, size, Chalk.colorizeLegacy(title));

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

            meta.setDisplayName(Chalk.colorizeLegacy(name));
            meta.setLore(Chalk.colorizeLegacyLore(lore));

            if (enchanted) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DENSITY, 1, false);
            }

            item.setItemMeta(meta);
            menu.setItem(slot, item);
        }

        return menu;
    }
}