package one.hyro.paper.events;

import one.hyro.paper.enums.PersistentDataKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PersistentDataContainer container = player.getPersistentDataContainer();

        NamespacedKey key = PersistentDataKeys.CUSTOM_MENU.getKey();
        if (container.has(key, PersistentDataType.STRING)) {
            event.setCancelled(true);
            player.getPersistentDataContainer().remove(key);
            player.closeInventory();
        }
     }
}
