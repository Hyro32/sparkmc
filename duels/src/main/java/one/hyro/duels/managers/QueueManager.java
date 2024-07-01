package one.hyro.duels.managers;

import one.hyro.duels.HyroDuels;
import one.hyro.duels.enums.DuelMode;
import one.hyro.duels.instances.DuelGameSession;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class QueueManager {
    private static QueueManager instance;
    private GameManager gameManager = GameManager.getInstance();
    private Map<UUID, DuelMode> singlesQueue;
    private Map<UUID, DuelMode> doublesQueue;
    private final Plugin plugin = HyroDuels.getInstance();

    private QueueManager() {
        this.singlesQueue = new HashMap<>();
        this.doublesQueue = new HashMap<>();
    }

    public void addPlayerToSingleQueue(UUID uuid, DuelMode mode) {
        if (singlesQueue.containsKey(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.sendMessage("You are already in the queue!");
            return;
        }
        singlesQueue.put(uuid, mode);
    }

    public void addPlayerToDoubleQueue(UUID uuid, DuelMode mode) {
        if (doublesQueue.containsKey(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.sendMessage("You are already in the queue!");
            return;
        }
        doublesQueue.put(uuid, mode);
    }

    public void removePlayerFromQueue(UUID uuid) {
        singlesQueue.remove(uuid);
        doublesQueue.remove(uuid);
    }

    public boolean isPlayerInQueue(UUID uuid) {
        return singlesQueue.containsKey(uuid) || doublesQueue.containsKey(uuid);
    }

    public List<UUID> getPlayersInSingleQueueByMode(DuelMode mode) {
        return singlesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<UUID> getPlayersInDoubleQueueByMode(DuelMode mode) {
        return doublesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void fillQueueOrCreateNew(DuelMode mode, int playersPerGame, Map<UUID, DuelMode> queue) {
        Iterator<UUID> playerUuids = queue.keySet().iterator();

        for (GameSession gameSession : gameManager.getGameSessions()) {
            if (gameSession.getMinigame() != HyroDuels.getInstance()) continue;

            DuelGameSession duelGame = (DuelGameSession) gameSession;
            if (duelGame.getPlayersUuids().size() < playersPerGame && duelGame.getMode() == mode) {
                UUID uuid = playerUuids.next();
                gameSession.addPlayer(uuid);
                playerUuids.remove();
                return;
            }
        }

        if (queue.size() >= playersPerGame) {
            DuelGameSession session = new DuelGameSession(
                    GameManager.getRandomMap(plugin),
                    playersPerGame,
                    playersPerGame,
                    HyroDuels.getInstance(),
                    mode
            );

            for (int i = 0; i < playersPerGame; i++) {
                UUID uuid = playerUuids.next();
                session.addPlayer(uuid);
                playerUuids.remove();
            }

            gameManager.registerGameSession(session);
            return;
        }

        DuelGameSession session = new DuelGameSession(
                GameManager.getRandomMap(plugin),
                playersPerGame,
                playersPerGame,
                HyroDuels.getInstance(),
                mode
        );

        while (playerUuids.hasNext()) {
            UUID uuid = playerUuids.next();
            session.addPlayer(uuid);
            playerUuids.remove();
        }

        gameManager.registerGameSession(session);
    }

    public void fillSingleDuelsOrCreateNew(DuelMode mode) {
        fillQueueOrCreateNew(mode, 2, singlesQueue);
    }

    public void fillDoublesDuelsOrCreateNew(DuelMode mode) {
        fillQueueOrCreateNew(mode, 4, doublesQueue);
    }

    public static QueueManager getInstance() {
        if (instance == null) instance = new QueueManager();
        return instance;
    }
}
