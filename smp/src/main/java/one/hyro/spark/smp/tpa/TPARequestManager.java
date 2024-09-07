package one.hyro.spark.smp.tpa;

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

import java.util.HashMap;
import java.util.Map;

public class TPARequestManager implements CommandExecutor {

    private final JavaPlugin plugin;
    private final String prefix;
    private final Map<Player, Player> tpaRequests = new HashMap<>();

    public TPARequestManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatUtils.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length != 1) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /tpa <playername>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                player.sendMessage(prefix + ChatColor.RED + "Player not found.");
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(prefix + ChatColor.RED + "You cannot send a teleport request to yourself.");
                return true;
            }

            // Send teleport request
            tpaRequests.put(target, player);
            player.sendMessage(prefix + ChatColor.GREEN + "Teleport request sent to " + target.getName());
            target.sendMessage(prefix + ChatColor.YELLOW + player.getName() + " has requested to teleport to you. Type /tpaccept or /tpdeny.");

            return true;
        }

        if (command.getName().equalsIgnoreCase("tpaccept")) {
            if (!tpaRequests.containsKey(player)) {
                player.sendMessage(prefix + ChatColor.RED + "You have no pending teleport requests.");
                return true;
            }

            Player requester = tpaRequests.get(player);
            startTeleportCountdown(requester, player);
            tpaRequests.remove(player);
            return true;
        }

        if (command.getName().equalsIgnoreCase("tpdeny")) {
            if (!tpaRequests.containsKey(player)) {
                player.sendMessage(prefix + ChatColor.RED + "You have no pending teleport requests.");
                return true;
            }

            Player requester = tpaRequests.get(player);
            requester.sendMessage(prefix + ChatColor.RED + player.getName() + " has denied your teleport request.");
            tpaRequests.remove(player);
            return true;
        }

        return false;
    }

    private void startTeleportCountdown(Player requester, Player target) {
        requester.sendMessage(prefix + ChatColor.GREEN + "Teleportation request accepted. Teleporting in 3 seconds. Do not move!");
        new BukkitRunnable() {
            private int countdown = 3;
            private final Location initialLocation = requester.getLocation().clone();

            @Override
            public void run() {
                if (!requester.isOnline() || !target.isOnline()) {
                    cancel();
                    return;
                }

                // Check if the requester has moved
                if (!requester.getLocation().getBlock().equals(initialLocation.getBlock())) {
                    requester.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    cancel();
                    return;
                }

                // Countdown logic
                if (countdown > 0) {
                    String countdownBar = ChatColor.RED + "Teleporting in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                    requester.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(countdownBar));
                    countdown--;
                } else {
                    // Perform teleportation if requester has not moved
                    if (requester.getLocation().getBlock().equals(initialLocation.getBlock())) {
                        requester.teleport(target.getLocation());
                        String actionBar = ChatColor.GREEN + "Teleported!";
                        requester.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    } else {
                        requester.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second (20 ticks)
    }
}
