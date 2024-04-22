package one.hyro.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import one.hyro.lib.i18n.Locales;
import one.hyro.lib.services.PlayersService;

public class PlayerDisconnectListener implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayersService.updatePlayerEntryLastJoined(player.getUniqueId());

        String playerCacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());
        Locales playerLocale = Locales.valueOf(playerCacheLocale);
        PlayersService.updatePlayerEntryLocale(player.getUniqueId(), playerLocale);
    }
}
