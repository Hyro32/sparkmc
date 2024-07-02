package one.hyro.duels.instances;

import lombok.Getter;
import lombok.Setter;
import one.hyro.duels.enums.DuelMode;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class DuelGameSession extends GameSession {
    private final DuelMode mode;
    @Setter
    private List<UUID> losers;

    public DuelGameSession(GameMap map, int minPlayers, int maxPlayers, Minigame minigame, DuelMode mode) {
        super(map, minPlayers, maxPlayers, minigame);
        this.mode = mode;
        this.losers = new ArrayList<>();
    }
}
