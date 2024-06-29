package one.hyro.duels.instances;

import lombok.Getter;
import one.hyro.duels.enums.DuelMode;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;

@Getter
public class DuelGameSession extends GameSession {
    private final DuelMode mode;

    public DuelGameSession(GameMap map, int minPlayers, int maxPlayers, Minigame minigame, DuelMode mode) {
        super(map, minPlayers, maxPlayers, minigame);
        this.mode = mode;
    }
}
