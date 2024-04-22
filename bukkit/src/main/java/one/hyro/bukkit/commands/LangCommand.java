package one.hyro.bukkit.commands;

import one.hyro.bukkit.utils.TabCompletion;
import one.hyro.lib.i18n.Locales;
import one.hyro.lib.i18n.I18n;
import one.hyro.lib.services.PlayersService;
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
            String cacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());

            if (args.length == 0) {
                player.sendMessage(I18n.getTranslation(cacheLocale, "errors.language.noArgs"));
                return false;
            }

            if (!Locales.exists(args[0])) {
                player.sendMessage(I18n.getTranslation(cacheLocale, "errors.language.notFound"));
                return false;
            }

            Locales newLocale = Locales.valueOf(args[0]);
            PlayersService.setCachePlayerLocale(player.getUniqueId(), newLocale);
            String newLocaleName = newLocale.getFullName();
            player.sendMessage(String.format(I18n.getTranslation(newLocale.toString(), "language.changed.message"), newLocaleName));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return TabCompletion.availableLocalesComplete();
        return null;
    }
}
