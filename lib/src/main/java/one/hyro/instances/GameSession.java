package one.hyro.instances;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import one.hyro.enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameSession {
    private final Minigame minigame;
    private GameStatus status;
    private final GameMap map;
    private final int minPlayers;
    private final int maxPlayers;
    private List<UUID> playersUuids;

    public GameSession(GameMap map, int minPlayers, int maxPlayers, Minigame minigame) {
        this.minigame = minigame;
        this.map = map;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playersUuids = new ArrayList<>();
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

    public void addPlayer(UUID uuid) {
        if (status != GameStatus.WAITING) return;
        playersUuids.add(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.getInventory().clear();
        player.teleportAsync(map.getWorld().getSpawnLocation()).thenAccept(success -> {
            if (!success) return;
            Component message = Component.translatable(
                    "info.status.join",
                    Component.text(playersUuids.size()),
                    Component.text(maxPlayers)
            );

            for (UUID playerUuid : playersUuids) {
                Player p = Bukkit.getPlayer(playerUuid);
                if (p == null) continue;
                p.sendMessage(message);
            }
        });

        GameStatus status = playersUuids.size() >= minPlayers ? GameStatus.STARTING : GameStatus.WAITING;
        setGameStatus(status);
    }

    public void removePlayer(UUID uuid) {
        playersUuids.remove(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.teleportAsync(Bukkit.getWorld("world").getSpawnLocation()).thenAccept(success -> {
            if (!success) return;
            Component message = Component.translatable(
                    "info.status.leave",
                    Component.text(playersUuids.size()),
                    Component.text(maxPlayers)
            );

            for (UUID playerUuid : playersUuids) {
                Player p = Bukkit.getPlayer(playerUuid);
                if (p == null) continue;
                p.sendMessage(message);
            }
        });

        if (status == GameStatus.STARTING && playersUuids.size() < minPlayers) setGameStatus(GameStatus.WAITING);
        if (status != GameStatus.WAITING && playersUuids.isEmpty()) setGameStatus(GameStatus.ENDING);
    }

    public boolean isPlayerInGame(UUID uuid) {
        return playersUuids.contains(uuid);
    }
}
