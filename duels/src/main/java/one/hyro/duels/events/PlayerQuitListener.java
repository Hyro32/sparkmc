package one.hyro.duels.events;

import one.hyro.duels.instances.DuelGameSession;
import one.hyro.duels.managers.QueueManager;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        QueueManager queueManager = QueueManager.getInstance();
        GameManager gameManager = GameManager.getInstance();

        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (queueManager.isPlayerInQueue(playerUuid)) {
            queueManager.removePlayerFromQueue(playerUuid);
        }

        for (GameSession session : gameManager.getGameSessions()) {
            if (session.getPlayersUuids().contains(playerUuid)) {
                DuelGameSession duelSession = (DuelGameSession) session;
                duelSession.getLosers().add(playerUuid);
                duelSession.setGameStatus(GameStatus.ENDING);
            }
        }
    }
}
