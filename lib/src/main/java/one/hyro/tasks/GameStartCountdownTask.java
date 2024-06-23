package one.hyro.tasks;

import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCountdownTask extends BukkitRunnable {
    private final Plugin plugin;
    private final GameSession session;
    private int seconds = 15;

    public GameStartCountdownTask(Plugin plugin, GameSession session) {
        this.plugin = plugin;
        this.session = session;
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (seconds <= 0) {
            this.cancel();
            for (Player player : session.getPlayers())
                player.sendMessage("Game starting!");
            session.setGameStatus(GameStatus.IN_GAME);
        }

        if (seconds == 10 || seconds <= 5) {
            session.getPlayers().forEach(player -> player.sendMessage("Game starting in " + seconds + " seconds!"));
        }
    }
}
