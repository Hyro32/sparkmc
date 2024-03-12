package dev.mardroide.bukkit.commands;

import dev.mardroide.lib.enums.Languages;
import dev.mardroide.lib.i18n.I18n;
import dev.mardroide.lib.utils.TabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class LangCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) return false;

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(I18n.getTranslation("en", "errors.language.noArgs"));
                return false;
            }

            if (!Languages.exists(args[0])) {
                player.sendMessage(I18n.getTranslation("en", "errors.language.notFound"));
                return false;
            }

            player.sendMessage(String.format(I18n.getTranslation("en", "language.changed.message"), args[0].toLowerCase()));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return TabComplete.languagesComplete();
        return null;
    }
}
