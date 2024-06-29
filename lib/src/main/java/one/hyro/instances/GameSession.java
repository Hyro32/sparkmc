package one.hyro.instances;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import one.hyro.enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameSession {
    private final Minigame minigame;
    private GameStatus status;
    private final GameMap map;
    private final int minPlayers;
    private final int maxPlayers;
    private List<Player> players;

    public GameSession(GameMap map, int minPlayers, int maxPlayers, Minigame minigame) {
        this.minigame = minigame;
        this.map = map;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.status = GameStatus.WAITING;
        minigame.waiting(this);
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
        player.getInventory().clear();

        player.teleportAsync(map.getWorld().getSpawnLocation()).thenAccept(success -> {
            if (success) {
                Component message = Component.translatable(
                        "info.status.join",
                        Component.text(players.size()),
                        Component.text(maxPlayers)
                );
                players.forEach(p -> p.sendMessage(message));
            }
        });

        GameStatus status = players.size() >= minPlayers ? GameStatus.STARTING : GameStatus.WAITING;
        setGameStatus(status);
    }

    public void removePlayer(Player player) {
        players.remove(player);

        player.teleportAsync(Bukkit.getWorld("world").getSpawnLocation()).thenAccept(success -> {
            if (success) {
                Component message = Component.translatable(
                        "info.status.leave",
                        Component.text(players.size()),
                        Component.text(maxPlayers)
                );
                players.forEach(p -> p.sendMessage(message));
            }
        });

        if (status == GameStatus.STARTING && players.size() < minPlayers) setGameStatus(GameStatus.WAITING);
        if (status != GameStatus.WAITING && players.isEmpty()) setGameStatus(GameStatus.ENDING);
    }

    public boolean isPlayerInGame(Player player) {
        return players.contains(player);
    }
}
