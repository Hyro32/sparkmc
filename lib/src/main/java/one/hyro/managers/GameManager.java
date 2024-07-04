package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameManager {
    private static GameManager instance;
    private final List<GameSession> gameSessions;

    private GameManager() {
        gameSessions = new ArrayList<>();
    }

    public void registerGameSession(GameSession gameSession) {
        gameSessions.add(gameSession);
    }

    public void unregisterGameSession(GameSession gameSession) {
        gameSessions.remove(gameSession);
    }

    public GameSession getGameSession(UUID uuid) {
        for (GameSession gameSession : gameSessions)
            if (gameSession.isPlayerInGame(uuid)) return gameSession;
        return null;
    }

    public boolean isPlayerInGame(UUID uuid) {
        for (GameSession gameSession : gameSessions)
            if (gameSession.isPlayerInGame(uuid)) return true;
        return false;
    }

    public static GameMap getRandomMap(Plugin plugin) {
        File[] files = new File(plugin.getDataFolder(), "maps").listFiles();
        if (files == null) return null;
        File file = files[(int) (Math.random() * files.length)];
        return new GameMap(file.getName(), plugin);
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }
}
