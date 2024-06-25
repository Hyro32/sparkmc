package one.hyro.duels.events;

import one.hyro.builders.CustomItem;
import one.hyro.builders.GameMenu;
import one.hyro.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuManager manager = MenuManager.getInstance();

        GameMenu menu = manager.getMenu(event.getInventory());
        if (menu != null) {
            event.setCancelled(true);
            CustomItem item = menu.getItems().get(event.getSlot());
            if (item == null || item.getConsumer() == null) return;
            item.getConsumer().accept(player);
        }
    }
}
