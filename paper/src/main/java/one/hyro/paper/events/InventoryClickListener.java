package one.hyro.paper.events;

import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PersistentDataKeys;
import one.hyro.paper.managers.MenusManager;
import one.hyro.paper.utilities.ConfigParser;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PersistentDataContainer container = player.getPersistentDataContainer();

        NamespacedKey key = PersistentDataKeys.CUSTOM_MENU.getKey();
        if (container.has(key, PersistentDataType.STRING)) {
            event.setCancelled(true);
            String menuName = container.get(key, PersistentDataType.STRING);
            File menuFile = new File(HyrosPaper.getInstance().getDataFolder() + "/menus", menuName + ".yml");
            FileConfiguration menuConfig = YamlConfiguration.loadConfiguration(menuFile);

            ConfigParser.runItemCommands(menuConfig, "items", event.getSlot(), player);
            player.getPersistentDataContainer().remove(key);
            player.closeInventory();
        }
     }
}
