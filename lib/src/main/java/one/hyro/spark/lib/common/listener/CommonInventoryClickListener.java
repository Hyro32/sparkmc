package one.hyro.spark.lib.common.listener;

import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.builder.SparkMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CommonInventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (inventory.getHolder(false) instanceof SparkMenu sparkMenu) {
            if (!sparkMenu.getItems().containsKey(slot)) return;
            SparkItem item = sparkMenu.getItems().get(slot);

            event.setCancelled(true);
            if (item.getClickConsumer() != null) item.getClickConsumer().accept(player);
        }
    }
}
