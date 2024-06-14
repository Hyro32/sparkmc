package one.hyro.paper.events;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class AsyncChatPlayerListener implements Listener, ChatRenderer {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this);
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(message.color(NamedTextColor.DARK_GRAY));
    }
}
