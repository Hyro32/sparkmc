package one.hyro.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import one.hyro.lib.enums.SanctionTypes;
import one.hyro.lib.i18n.I18n;
import one.hyro.lib.services.PlayersService;
import one.hyro.lib.services.SanctionsService;

public class PostLoginListener implements Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        String sanction = SanctionsService.getSanctionEntryByUuidAndType(player.getUniqueId(), SanctionTypes.BAN);
        System.out.println(sanction);

        if (sanction != null) {
            String playerCacheLocale = PlayersService.getCachePlayerLocale(player.getUniqueId());
            player.disconnect(I18n.getTranslation(playerCacheLocale, "moderation.ban.login"));
            return;
        }

        PlayersService.createPlayerEntry(player.getUniqueId());
    }
}
