package one.hyro.spark.lib.session;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import one.hyro.spark.lib.interfaces.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

abstract public class Session {
    private final Minigame minigame;
    @Getter private List<UUID> uuids;
    @Getter private SessionStatus status;
    @Getter private final SessionMap map;
    @Getter private final int maxPlayers;
    @Getter private final int minPlayers;

    public Session(Minigame minigame, int maxPlayers, int minPlayers) {
        this.minigame = minigame;
        this.uuids = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.status = SessionStatus.WAITING;
        this.map = new SessionMap();
        init();
    }

    private void init() {
        if (minPlayers > maxPlayers) throw new IllegalArgumentException("Min players cannot be greater than max players");
        minigame.waiting(this);
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
        switch (status) {
            case WAITING -> minigame.waiting(this);
            case STARTING -> minigame.starting(this);
            case PLAYING -> minigame.playing(this);
            case ENDING -> minigame.ending(this);
        }
    }

    public void addPlayer(UUID uuid, Component component) {
        uuids.add(uuid);
        // Teleport player to the minigame session
        broadcast(component);
    }

    public void removePlayer(UUID uuid, Component component) {
        uuids.remove(uuid);
        // Teleport player to the lobby
        broadcast(component);
    }

    public void broadcast(Component component) {
        uuids.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.sendMessage(component);
        });
    }
}
