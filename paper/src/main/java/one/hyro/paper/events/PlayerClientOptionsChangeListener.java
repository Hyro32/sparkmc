package one.hyro.paper.events;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerClientOptionsChangeListener implements Listener {
    @EventHandler
    public void onPlayerClientOptionsChange(PlayerClientOptionsChangeEvent event) {
        Player player = event.getPlayer();
        if (event.hasLocaleChanged()) {
            Component localeMessage = Component.translatable(
                    "info.player.locale",
                    Component.text(event.getLocale()).color(NamedTextColor.RED)
            ).color(NamedTextColor.GOLD);
            player.sendMessage(localeMessage);
        }
    }
}
