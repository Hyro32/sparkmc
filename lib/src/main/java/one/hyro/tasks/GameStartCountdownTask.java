package one.hyro.tasks;

import one.hyro.managers.GameManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCountdownTask extends BukkitRunnable {
    private final Plugin plugin;
    private final GameManager manager;
    private int seconds = 15;

    public GameStartCountdownTask(Plugin plugin, GameManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (seconds <= 0) {
            Thread.currentThread().interrupt();
        }

        // Get game session and broadcast countdown
    }
}
