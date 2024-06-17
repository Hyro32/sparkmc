package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public GameSession getGameSession(UUID uuid) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.isPlayerInGame(uuid)) {
                return gameSession;
            }
        }
        return null;
    }
}
