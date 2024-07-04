package one.hyro.tasks;

import one.hyro.instances.GameSession;
import one.hyro.managers.BlockManager;
import one.hyro.managers.GameManager;
import one.hyro.utils.Teleport;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameEndCountdownTask extends BukkitRunnable {
    private final GameSession session;
    private int seconds = 15;

    public GameEndCountdownTask(Plugin plugin, GameSession session) {
        this.session = session;
        this.runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (session.getPlayersUuids().isEmpty()) {
            this.cancel();
            session.getMap().unload();
            return;
        }

        if (seconds <= 0) {
            this.cancel();

            for (UUID uuid : session.getPlayersUuids()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                Teleport.teleportToLobby(player, null);
            }

            session.getMap().unload();
            GameManager gameManager = GameManager.getInstance();
            BlockManager blockManager = BlockManager.getInstance();
            gameManager.unregisterGameSession(session);
            blockManager.removeBlocksFromSession(session);
        }
    }
}
