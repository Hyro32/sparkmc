package dev.mardroide.bukkit.commands;

import dev.mardroide.lib.i18n.I18n;
import dev.mardroide.lib.utils.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class KickCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        if (sender instanceof Player) {
            if (!sender.hasPermission("moderation.kick") || !sender.isOp()) {
                sender.sendMessage(I18n.getTranslation("en", "errors.permission.noPermission"));
                return false;
            }

            if (target == null) {
                sender.sendMessage(I18n.getTranslation("en", "errors.player.notFound"));
                return false;
            }

            target.kickPlayer(I18n.getTranslation("en", "moderation.kick.message"));
            sender.sendMessage(I18n.getTranslation("en", "moderation.kick.success"));
        } else {
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return false;
            }

            target.kickPlayer(I18n.getTranslation("en", "moderation.kick.message"));
            System.out.println(ChatColor.GREEN + "Player has been kicked.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return TabComplete.playersNamesComplete();
        return null;
    }
}
