package one.hyro.paper.events;

import one.hyro.paper.managers.MenusManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().isRightClick()) {
            if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                event.setCancelled(true);
                MenusManager.openMenu(player, "minigames");
            }
        }
    }
}
