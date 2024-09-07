package one.hyro.spark.smp.spawn;

import one.hyro.spark.smp.SparkSmp;
import one.hyro.spark.smp.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SpawnManager implements CommandExecutor {

    private final String prefix;

    public SpawnManager() {
        this.prefix = ChatUtils.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;
        Location spawnLocation = new Location(Bukkit.getWorld("world"), -44, 72, 43, 0.0f, 0.0f);

        teleportToSpawn(player, spawnLocation);
        return true;
    }

    private void teleportToSpawn(Player player, Location spawnLocation) {
        Location initialLocation = player.getLocation().clone();
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            private int countdown = 3;

            @Override
            public void run() {
                if (!player.isOnline() || player.getUniqueId() != playerId) {
                    cancel();
                    return;
                }

                // Check if the player has moved
                if (!player.getLocation().equals(initialLocation)) {
                    player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    cancel();
                    return;
                }

                // Countdown logic
                if (countdown > 0) {
                    String actionBar = ChatColor.RED + "Teleporting to spawn in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    countdown--;
                } else {
                    // Perform teleportation if the player has not moved
                    if (player.getLocation().equals(initialLocation)) {
                        player.teleport(spawnLocation);
                        String actionBar = ChatColor.GREEN + "Teleported to spawn!";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(SparkSmp.class), 0L, 20L); // Check every second (20 ticks)
    }
}
