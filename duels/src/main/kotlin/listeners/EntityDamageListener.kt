package one.hyro.listeners

import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object EntityDamageListener: Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity: Entity = event.entity
        if (entity !is Player) return

        val player: Player = entity

        if (player.health - event.finalDamage <= 0) {
            event.isCancelled = true
            player.gameMode = GameMode.SPECTATOR

            // TODO: Send winner and loser messages
        }
    }
}