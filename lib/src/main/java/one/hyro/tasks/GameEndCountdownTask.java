package one.hyro.tasks;

import one.hyro.instances.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndCountdownTask extends BukkitRunnable {
    private final Plugin plugin;
    private final GameSession session;
    private int seconds = 15;

    public GameEndCountdownTask(Plugin plugin, GameSession session) {
        this.plugin = plugin;
        this.session = session;
        this.runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (seconds <= 0) {
            this.cancel();
            for (Player player : session.getPlayers()) {
                player.teleportAsync(Bukkit.getWorld("world").getSpawnLocation()).thenAccept(success -> {
                    if (success) {
                        player.getInventory().clear();
                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.setInvulnerable(true);
                    }
                });
            }
        }
    }
}
