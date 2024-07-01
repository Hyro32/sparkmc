package one.hyro.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameStartCountdownTask extends BukkitRunnable {
    private final Plugin plugin;
    private final GameSession session;
    private int seconds = 15;

    public GameStartCountdownTask(Plugin plugin, GameSession session) {
        this.plugin = plugin;
        this.session = session;
        this.runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (session.getPlayersUuids().size() < session.getMinPlayers()) {
            this.cancel();
            session.setGameStatus(GameStatus.WAITING);
        }

        if (seconds <= 0) {
            this.cancel();
            for (UUID uuid : session.getPlayersUuids()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Location location = player.getLocation();
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                player.sendMessage(Component.text("Game starting!", NamedTextColor.GREEN));
            }

            session.setGameStatus(GameStatus.IN_GAME);
        }

        if (seconds == 10 || (seconds >= 1 && seconds <= 5)) {
            for (UUID uuid : session.getPlayersUuids()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Location location = player.getLocation();
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

                Component message = Component.text("Game starting in ", NamedTextColor.GOLD)
                        .append(Component.text(seconds, NamedTextColor.RED))
                        .append(Component.text(" seconds!", NamedTextColor.GOLD));

                player.sendMessage(message);
            }
        }
    }
}
