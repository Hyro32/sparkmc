package one.hyro.duels.managers;

import lombok.Getter;
import one.hyro.duels.HyroDuels;
import one.hyro.duels.enums.DuelMode;
import one.hyro.duels.instances.DuelGameSession;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
public class QueueManager {
    private static QueueManager instance;
    private final GameManager gameManager = GameManager.getInstance();
    private final Map<Player, DuelMode> singlesQueue;
    private final Map<Player, DuelMode> doublesQueue;
    private final Plugin plugin = HyroDuels.getInstance();

    private QueueManager() {
        this.singlesQueue = new HashMap<>();
        this.doublesQueue = new HashMap<>();
    }

    public void addPlayerToSingleQueue(Player player, DuelMode mode) {
        if (singlesQueue.containsKey(player)) {
            player.sendMessage("You are already in the queue!");
            return;
        }
        singlesQueue.put(player, mode);
    }

    public void addPlayerToDoubleQueue(Player player, DuelMode mode) {
        if (doublesQueue.containsKey(player)) {
            player.sendMessage("You are already in the queue!");
            return;
        }
        doublesQueue.put(player, mode);
    }

    public void removePlayerFromQueue(Player player) {
        if (singlesQueue.containsKey(player)) singlesQueue.remove(player);
        if (doublesQueue.containsKey(player)) doublesQueue.remove(player);
    }

    public boolean isPlayerInQueue(Player player) {
        return singlesQueue.containsKey(player) || doublesQueue.containsKey(player);
    }

    public List<Player> getPlayersInSingleQueueByMode(DuelMode mode) {
        return singlesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Player> getPlayersInDoubleQueueByMode(DuelMode mode) {
        return doublesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void fillQueueOrCreateNew(DuelMode mode, int playersPerGame, Map<Player, DuelMode> queue) {
        Iterator<Player> players = queue.keySet().iterator();

        for (GameSession gameSession : gameManager.getGameSessions()) {
            if (gameSession.getMinigame() != HyroDuels.getInstance()) continue;

            DuelGameSession duelGame = (DuelGameSession) gameSession;
            if (duelGame.getPlayers().size() < playersPerGame && duelGame.getMode() == mode) {
                Player player = players.next();
                gameSession.addPlayer(player);
                players.remove();
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
                Player player = players.next();
                session.addPlayer(player);
                players.remove();
            }

            gameManager.registerGameSession(session);
            session.setGameStatus(GameStatus.STARTING);
            return;
        }

        DuelGameSession session = new DuelGameSession(
                GameManager.getRandomMap(plugin),
                playersPerGame,
                playersPerGame,
                HyroDuels.getInstance(),
                mode
        );

        while (players.hasNext()) {
            Player player = players.next();
            session.addPlayer(player);
            players.remove();
        }

        gameManager.registerGameSession(session);
    }

    public void fillSingleDuelsOrCreateNew(DuelMode mode) {
        fillQueueOrCreateNew(mode, 1, singlesQueue);
    }

    public void fillDoublesDuelsOrCreateNew(DuelMode mode) {
        fillQueueOrCreateNew(mode, 4, doublesQueue);
    }

    public static QueueManager getInstance() {
        if (instance == null) instance = new QueueManager();
        return instance;
    }
}
