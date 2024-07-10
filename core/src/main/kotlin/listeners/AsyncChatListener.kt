package one.hyro.listeners

import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object AsyncChatListener: Listener, ChatRenderer {
    @EventHandler
    fun onAsyncChat(event: AsyncChatEvent) {
        event.renderer(this::render)
    }

    override fun render(player: Player, displayName: Component, message: Component, audience: Audience): Component {
        return displayName.color(NamedTextColor.DARK_GRAY)
            .append(Component.text(": ", NamedTextColor.DARK_GRAY))
            .append(message.color(NamedTextColor.GRAY))
    }
}