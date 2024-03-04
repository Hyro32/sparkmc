package dev.mardroide.bukkit.listeners;

import dev.mardroide.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandPreprocessListener implements Listener {
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        String[] args = command.split(" ");
        Player player = event.getPlayer();

        if (player.hasPermission("bukkit.blockedcommands.bypass") || player.isOp()) return;

        List<String> blockedCommands = Bukkit.getInstance().getConfig().getStringList("blocked-commands");

        if (blockedCommands.contains(args[0].toLowerCase())) {
            player.sendMessage("§cYou can't use this command!");
            event.setCancelled(true);
        }

        for (String blockedCommand : blockedCommands) {
            if (args[0].toLowerCase().startsWith(blockedCommand)) {
                player.sendMessage("§cYou can't use this command!");
                event.setCancelled(true);
                break;
            }
        }
    }
}
