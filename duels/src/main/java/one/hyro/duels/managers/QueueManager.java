package one.hyro.duels.managers;

import lombok.Getter;
import one.hyro.duels.HyroDuels;
import one.hyro.duels.enums.DuelMode;
import one.hyro.duels.instances.DuelGameSession;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueueManager {
    private static QueueManager instance;
    private final GameManager gameManager = GameManager.getInstance();
    private final Map<Player, DuelMode> singlesQueue;
    private final Map<Player, DuelMode> doublesQueue;

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

    public void fillSingleDuelsOrCreateNew(DuelMode mode) {
        List<Player> players = getPlayersInSingleQueueByMode(mode);

        for (GameSession gameSession : gameManager.getGameSessions()) {
            if (gameSession.getMinigame() != HyroDuels.getInstance()) continue;

            DuelGameSession duelGame = (DuelGameSession) gameSession;
            if (duelGame.getPlayers().size() < 2 && duelGame.getMode() == mode) {
                gameSession.addPlayer(players.getFirst());
                removePlayerFromQueue(players.getFirst());
                return;
            }
        }

        if (players.size() >= 2) {
            List<Player> playersToCreate = players.subList(0, 2);
            DuelGameSession session = new DuelGameSession(playersToCreate, new GameMap(), 2, 2, HyroDuels.getInstance(), mode);
            gameManager.registerGameSession(session);
            session.setGameStatus(GameStatus.STARTING);
            return;
        }

        List<Player> playersToCreate = players.subList(0, 1);
        DuelGameSession session = new DuelGameSession(playersToCreate, new GameMap(), 2, 2, HyroDuels.getInstance(), mode);
        gameManager.registerGameSession(session);
    }

    public void fillDoublesDuelsOrCreateNew(DuelMode mode) {
        List<Player> players = getPlayersInDoubleQueueByMode(mode);

        for (GameSession gameSession : gameManager.getGameSessions()) {
            if (gameSession.getMinigame() != HyroDuels.getInstance()) continue;

            DuelGameSession duelGame = (DuelGameSession) gameSession;
            if (duelGame.getPlayers().size() < 4 && duelGame.getMode() == mode) {
                gameSession.addPlayer(players.getFirst());
                players.removeFirst();
                return;
            }
        }

        if (players.size() >= 4) {
            List<Player> playersToCreate = players.subList(0, 4);
            playersToCreate.forEach(this::removePlayerFromQueue);
            DuelGameSession session = new DuelGameSession(playersToCreate, new GameMap(), 4, 4, HyroDuels.getInstance(), mode);
            gameManager.registerGameSession(session);
            session.setGameStatus(GameStatus.STARTING);
            return;
        }

        List<Player> playersToCreate = players.subList(0, players.size());
        DuelGameSession session = new DuelGameSession(playersToCreate, new GameMap(), 4, 4, HyroDuels.getInstance(), mode);
        gameManager.registerGameSession(session);
    }

    public static QueueManager getInstance() {
        if (instance == null) instance = new QueueManager();
        return instance;
    }
}
