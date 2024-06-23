package one.hyro.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        event.setCancelled(true);
        player.setFoodLevel(20);
    }
}
