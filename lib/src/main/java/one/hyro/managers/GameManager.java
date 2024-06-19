package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameSession;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameManager {
    private final List<GameSession> gameSessions;

    public GameManager() {
        gameSessions = new ArrayList<>();
    }

    public void registerGameSession(GameSession gameSession) {
        gameSessions.add(gameSession);
    }

    public void unregisterGameSession(GameSession gameSession) {
        gameSessions.remove(gameSession);
    }

    public GameSession getGameSession(Player player) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.isPlayerInGame(player)) {
                return gameSession;
            }
        }
        return null;
    }

    public boolean isInGame(Player player) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.isPlayerInGame(player)) {
                return true;
            }
        }
        return false;
    }
}
