package one.hyro.paper.events;

import one.hyro.builders.CustomItem;
import one.hyro.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        MenuManager manager = MenuManager.getInstance();

        CustomItem item = manager.getItem(event.getItem());
        if (event.getAction().isRightClick() && item != null) {
            event.setCancelled(true);
            item.getConsumer().accept(player);
        }
    }
}
