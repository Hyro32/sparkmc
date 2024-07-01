package one.hyro.duels.events;

import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);

            GameSession session = GameManager.getInstance().getGameSession(player.getUniqueId());
            session.setGameStatus(GameStatus.ENDING);
        }
    }
}
