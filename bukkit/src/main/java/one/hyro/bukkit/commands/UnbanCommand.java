package one.hyro.bukkit.commands;

import one.hyro.lib.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class UnbanCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();

        if (sender instanceof Player) {
            if (!sender.hasPermission("moderation.unban") || !sender.isOp()) {
                sender.sendMessage(I18n.getTranslation("en", "errors.permission.noPermission"));
                return false;
            }

            if (target == null) {
                sender.sendMessage(I18n.getTranslation("en", "errors.player.notFound"));
                return false;
            }

            // TODO: Call the API to check if the player is banned

            if (this != null) {
                // TODO: Call the API to unban the player (delete the document from the database)
                sender.sendMessage(I18n.getTranslation("en", "moderation.unban.success"));
            } else {
                sender.sendMessage(I18n.getTranslation("en", "errors.player.notBanned"));
                return false;
            }
        } else {
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return false;
            }

            // TODO: Call the API to check if the player is banned

            if (this != null) {
                // TODO: Call the API to unban the player (delete the document from the database)
                sender.sendMessage(ChatColor.GREEN + "Player has been unbanned.");
            } else {
                sender.sendMessage(ChatColor.RED + "Player is not banned.");
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
