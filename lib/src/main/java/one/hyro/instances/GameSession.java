package one.hyro.instances;

import lombok.Getter;
import one.hyro.enums.GameStatus;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class GameSession {
    private final Minigame minigame;
    private GameStatus status;
    private final GameMap map;
    private final List<Player> players;

    public GameSession(List<Player> players, GameMap map, Minigame minigame) {
        this.minigame = minigame;
        this.map = map;
        this.players = players;
        this.status = GameStatus.WAITING;
    }

    public void setGameStatus(GameStatus newStatus) {
        this.status = newStatus;
        switch (newStatus) {
            case WAITING -> minigame.waiting(this);
            case STARTING -> minigame.starting(this);
            case IN_GAME -> minigame.inGame(this);
            case ENDING -> minigame.ending(this);
        }
    }

    public void addPlayer(Player player) {
        if (status != GameStatus.WAITING) return;
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean isPlayerInGame(Player player) {
        return players.contains(player);
    }
}
