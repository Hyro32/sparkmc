package one.hyro.bukkit.listeners;

import one.hyro.bukkit.HyroBukkit;
import one.hyro.lib.i18n.I18n;
import one.hyro.lib.services.PlayersService;
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

        List<String> blockedCommands = HyroBukkit.getInstance().getConfig().getStringList("blocked-commands");
        String playerCacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());

        if (blockedCommands.contains(args[0].toLowerCase())) {
            player.sendMessage(I18n.getTranslation(playerCacheLocale, "errors.permission.noPermission"));
            event.setCancelled(true);
            return;
        }

        for (String blockedCommand : blockedCommands) {
            if (args[0].toLowerCase().startsWith(blockedCommand)) {
                player.sendMessage(I18n.getTranslation(playerCacheLocale, "errors.permission.noPermission"));
                event.setCancelled(true);
                break;
            }
        }
    }
}
