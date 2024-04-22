package one.hyro.bukkit.commands;

import one.hyro.lib.i18n.I18n;
import one.hyro.lib.i18n.Locales;
import one.hyro.lib.services.PlayersService;
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
        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            String locale = (sender instanceof Player)
                    ? PlayersService.getCachePlayerLocale(((Player) sender).getUniqueId())
                    : Locales.DEFAULT.toString();
            sender.sendMessage(I18n.getTranslation(locale, "errors.player.notFound"));
            return false;
        }

        String targetCacheLocale = PlayersService.getCachePlayerLocale(target.getUniqueId());

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String cacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());

            if (!player.hasPermission("moderation.kick") && !player.isOp()) {
                player.sendMessage(I18n.getTranslation(cacheLocale, "errors.permission.noPermission"));
                return false;
            }

            target.kickPlayer(I18n.getTranslation(targetCacheLocale, "moderation.kick.message"));
            player.sendMessage(I18n.getTranslation(cacheLocale, "moderation.kick.success"));
        } else {
            target.kickPlayer(I18n.getTranslation(targetCacheLocale, "moderation.kick.message"));
            sender.sendMessage(ChatColor.GREEN + "Player has been kicked.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
