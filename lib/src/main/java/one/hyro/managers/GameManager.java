package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
