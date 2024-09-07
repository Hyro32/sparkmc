package one.hyro.spark.smp.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemReplacementListener implements Listener {

    private final JavaPlugin plugin;

    public ItemReplacementListener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin); // Register the event listener
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check if the message contains [item]
        if (message.contains("[item]")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // Get the item name or display name
            String itemName = getItemDisplayName(itemInHand);

            // Replace [item] in the message
            String newMessage = message.replace("[item]", itemName);

            // Set the new message and cancel the original message
            event.setMessage(newMessage);
        }
    }

    private String getItemDisplayName(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return ChatColor.GRAY + "nothing"; // Return "nothing" if no item is held
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName(); // Return display name if set
        }

        return item.getType().toString(); // Return item type if no display name is set
    }
}
