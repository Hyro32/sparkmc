package dev.mardroide.bukkit.commands;

import dev.mardroide.lib.enums.Reasons;
import dev.mardroide.lib.i18n.I18n;
import dev.mardroide.lib.utils.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BanCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;
        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!sender.hasPermission("moderation.ban") || !sender.isOp()) {
                sender.sendMessage(I18n.getTranslation("en", "errors.permission.noPermission"));
                return false;
            }

            if (target == null) {
                sender.sendMessage(I18n.getTranslation("en", "errors.player.notFound"));
                return false;
            }

            Date expirationDate = null;
            String reason = null;
            if (args.length >= 2) {
                if (isDuration(args[1])) {
                    expirationDate = getExpirationDate(args[1]);
                    if (args.length >= 3) {
                        reason = getReason(Arrays.copyOfRange(args, 2, args.length));
                    }
                } else {
                    reason = getReason(Arrays.copyOfRange(args, 1, args.length));
                }
            }

            target.kickPlayer(I18n.getTranslation("en", "moderation.ban.message"));
            // TODO: Call the API to create the ban document
            sender.sendMessage(I18n.getTranslation("en", "moderation.ban.success"));
        } else {
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return false;
            }

            Date expirationDate = null;
            String reason = null;
            if (args.length >= 2) {
                if (isDuration(args[1])) {
                    expirationDate = getExpirationDate(args[1]);
                    if (args.length >= 3) {
                        reason = getReason(Arrays.copyOfRange(args, 2, args.length));
                    }
                } else {
                    reason = getReason(Arrays.copyOfRange(args, 1, args.length));
                }
            }

            target.kickPlayer(I18n.getTranslation("en", "moderation.ban.message"));
            // TODO: Call the API to create the ban document
            sender.sendMessage(ChatColor.GREEN + "Player has been banned from the server.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return TabComplete.playersNamesComplete();
        if (args.length >= 2) return TabComplete.defaultModerationReasons();
        return null;
    }

    private Date getExpirationDate(String duration) {
        try {
            long time = parseDuration(duration);
            return new Date(System.currentTimeMillis() + time);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getReason(String[] reasonArgs) {
        String reasonKey = String.join(" ", reasonArgs).toUpperCase();
        try {
            Reasons defaultReason = Reasons.valueOf(reasonKey);
            return I18n.getTranslation("en", defaultReason.getLocaleKey());
        } catch (IllegalArgumentException e) {
            return String.join(" ", reasonArgs);
        }
    }

    private long parseDuration(String duration) {
        String[] parts = duration.split(" ");
        long totalMillis = 0;

        for (String part : parts) {
            char timeType = Character.toLowerCase(part.charAt(part.length() - 1));
            long value = Long.parseLong(part.substring(0, part.length() - 1));

            switch (timeType) {
                case 'd':
                    totalMillis += value * 86400 * 1000;
                    break;
                case 'h':
                    totalMillis += value * 3600 * 1000;
                    break;
                case 'm':
                    totalMillis += value * 60 * 1000;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid duration format");
            }
        }

        return totalMillis;
    }

    private boolean isDuration(String arg) {
        return arg.endsWith("d") || arg.endsWith("h") || arg.endsWith("m");
    }
}
