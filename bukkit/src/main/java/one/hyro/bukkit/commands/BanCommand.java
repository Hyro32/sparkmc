package one.hyro.bukkit.commands;

import one.hyro.bukkit.utils.TabCompletion;
import one.hyro.lib.enums.SanctionTypes;
import one.hyro.lib.i18n.I18n;
import one.hyro.lib.i18n.Locales;
import one.hyro.lib.services.PlayersService;
import one.hyro.lib.services.SanctionsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public class BanCommand implements TabExecutor {
    private String reason = null;
    private Date expirationDate = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;
        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();

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
            String playerCacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());

            if (!player.hasPermission("moderation.ban") || !sender.isOp()) {
                player.sendMessage(I18n.getTranslation(playerCacheLocale, "errors.permission.noPermission"));
                return false;
            }

            target.kickPlayer(I18n.getTranslation(targetCacheLocale, "moderation.ban.message"));
            SanctionsService.createSanctionEntry(target.getUniqueId(), player.getUniqueId(), SanctionTypes.BAN, reason, expirationDate);
            sender.sendMessage(I18n.getTranslation(playerCacheLocale, "moderation.ban.success"));
        } else {
            target.kickPlayer(I18n.getTranslation(targetCacheLocale, "moderation.ban.message"));
            SanctionsService.createSanctionEntry(target.getUniqueId(), null, SanctionTypes.BAN, reason, expirationDate);
            sender.sendMessage(ChatColor.GREEN + "Player has been banned from the server.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return TabCompletion.playersNamesComplete();
        if (args.length >= 2) return TabCompletion.defaultModerationReasons();
        return null;
    }
}
