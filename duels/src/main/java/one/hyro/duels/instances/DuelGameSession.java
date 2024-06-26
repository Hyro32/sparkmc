package one.hyro.duels.instances;

import lombok.Getter;
import one.hyro.duels.enums.DuelMode;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class DuelGameSession extends GameSession {
    private final DuelMode mode;

    public DuelGameSession(List<Player> players, GameMap map, Minigame minigame, DuelMode mode) {
        super(players, map, minigame);
        this.mode = mode;
    }
}
