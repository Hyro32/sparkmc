package one.hyro.spark.lib.common.listener;

import one.hyro.spark.lib.SparkLib;
import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.builder.SparkMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class SparkInventoryListener implements Listener {
    private BukkitTask updateTask;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (inventory.getHolder(false) instanceof SparkMenu sparkMenu) {
            if (!sparkMenu.getItems().containsKey(slot)) return;
            SparkItem item = sparkMenu.getItems().get(slot);

            event.setCancelled(true);
            if (item.getLeftClickConsumer() != null && event.isLeftClick()) {
                item.getLeftClickConsumer().accept(player);
            } else if (item.getRightClickConsumer() != null && event.isRightClick()) {
                item.getRightClickConsumer().accept(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof SparkMenu) {
            if (updateTask != null && !updateTask.isCancelled()) {
                updateTask.cancel();
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof SparkMenu sparkMenu) {
            updateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sparkMenu.build();
                }
            }.runTaskTimer(SparkLib.getPlugin(), 0L, 20L);
        }
    }
}