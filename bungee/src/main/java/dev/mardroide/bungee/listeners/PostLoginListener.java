package dev.mardroide.bungee.listeners;

import dev.mardroide.lib.i18n.I18n;
import dev.mardroide.lib.jdbc.collections.ModerationCollection;
import dev.mardroide.lib.jdbc.collections.PlayersCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;

public class PostLoginListener implements Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (PlayersCollection.find(player.getUniqueId()) == null) {
            PlayersCollection.create(player.getUniqueId());
        }

        Document sanction = ModerationCollection.find(player.getUniqueId());

        if (sanction != null) {
            player.disconnect(I18n.getTranslation("en", "moderation.ban.login"));
            return;
        }
    }
}
