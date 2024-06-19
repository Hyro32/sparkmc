package one.hyro.duels.managers;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QueueManager {
    private final List<Player> singlesQueue;
    private final List<Player> doublesQueue;

    public QueueManager() {
        this.singlesQueue = new ArrayList<>();
        this.doublesQueue = new ArrayList<>();
    }

    public void addToSinglesQueue(Player player) {
        if (singlesQueue.contains(player)) return;
        singlesQueue.add(player);
    }

    public void addToDoublesQueue(Player player) {
        if (doublesQueue.contains(player)) return;
        doublesQueue.add(player);
    }

    public void removeFromQueue(Player player) {
        if (!singlesQueue.contains(player)) return;
        singlesQueue.remove(player);
    }

    public void removeFromDoublesQueue(Player player) {
        if (!doublesQueue.contains(player)) return;
        doublesQueue.remove(player);
    }
}
