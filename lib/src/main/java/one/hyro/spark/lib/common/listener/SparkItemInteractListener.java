package one.hyro.spark.lib.common.listener;

import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.helper.registry.SparkItemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SparkItemInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        SparkItem sparkItem = SparkItemRegistry.getInstance().getSparkItem(item);
        System.out.println("Spark! ");

        if (event.getAction().isRightClick() && sparkItem != null) {
            event.setCancelled(true);

            if (sparkItem.getInteractConsumer() != null) {
                sparkItem.getInteractConsumer().accept(player);
            }
        }
    }
}
