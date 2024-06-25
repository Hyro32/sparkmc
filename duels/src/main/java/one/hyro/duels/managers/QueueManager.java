package one.hyro.duels.managers;

import lombok.Getter;
import one.hyro.duels.enums.DuelMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueueManager {
    private final Map<Player, DuelMode> singlesQueue;
    private final Map<Player, DuelMode> doublesQueue;

    public QueueManager() {
        this.singlesQueue = new HashMap<>();
        this.doublesQueue = new HashMap<>();
    }

    public void addPlayerToSingleQueue(Player player, DuelMode mode) {
        if (singlesQueue.containsKey(player)) {
            player.sendMessage("You are already in the queue!");
            return;
        }
        singlesQueue.put(player, mode);
    }

    public void addPlayerToDoubleQueue(Player player, DuelMode mode) {
        if (doublesQueue.containsKey(player)) {
            player.sendMessage("You are already in the queue!");
            return;
        }
        doublesQueue.put(player, mode);
    }

    public void removePlayerFromSingleQueue(Player player) {
        if (!singlesQueue.containsKey(player)) {
            player.sendMessage("You are not in the queue!");
            return;
        }
        singlesQueue.remove(player);
    }

    public void removePlayerFromDoubleQueue(Player player) {
        if (!doublesQueue.containsKey(player)) {
            player.sendMessage("You are not in the queue!");
            return;
        }
        doublesQueue.remove(player);
    }

    public boolean isPlayerInQueue(Player player) {
        return singlesQueue.containsKey(player) || doublesQueue.containsKey(player);
    }

    public List<Player> getPlayersInSingleQueueByMode(DuelMode mode) {
        return singlesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Player> getPlayersInDoubleQueueByMode(DuelMode mode) {
        return doublesQueue.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mode))
                .map(Map.Entry::getKey)
                .toList();
    }
}
