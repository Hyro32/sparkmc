package one.hyro.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.managers.ScoreboardManager
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)
        val player = event.player
        player.gameMode = GameMode.ADVENTURE
        ScoreboardManager.setMainTab(player)
        ScoreboardManager.setRole(player)

        if (player.hasPermission("hyro.join") || player.isOp) {
            val joinMessage: Component = Component.text(player.name + " has joined the server!", NamedTextColor.YELLOW)
            val world: World = player.world
            world.players.forEach { worldPLayer: Player -> worldPLayer.sendMessage(joinMessage) }
        }
    }
}