package one.hyro.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.Queue
import one.hyro.minigame.Session
import one.hyro.registry.SessionsRegistry
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player

        if (Queue.isQueued(player.uniqueId)) {
            Queue.removeFromQueue(player.uniqueId)
        }

        if (SessionsRegistry.isPlayerInSession(player.uniqueId)) {
            val session: Session = SessionsRegistry.getSession(player.uniqueId)!!
            session.removePlayer(player.uniqueId, Component.text("${player.name()} left the session!", NamedTextColor.RED))
        }
    }
}