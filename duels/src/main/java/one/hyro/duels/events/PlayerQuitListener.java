package one.hyro.duels.events;

import one.hyro.duels.managers.QueueManager;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        QueueManager queueManager = QueueManager.getInstance();
        GameManager gameManager = GameManager.getInstance();
        System.out.println("Player quit");

        if (queueManager.isPlayerInQueue(event.getPlayer())) {
            queueManager.removePlayerFromQueue(event.getPlayer());
        }

        if (gameManager.isInGame(event.getPlayer())) {
            GameSession game = gameManager.getGameSession(event.getPlayer());
            game.removePlayer(event.getPlayer());
        }
    }
}
