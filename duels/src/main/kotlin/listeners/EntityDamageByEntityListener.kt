package one.hyro.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager: Entity = event.damager

        if (damager is Player) {
            val player: Player = event.entity as Player
            var health: Double = Math.round((player.health - event.finalDamage) * 10.0) / 10.0
            if (health < 0) health = 0.0

            val message: Component = Component.text(
                "${player.name} has $health health left!",
                NamedTextColor.GRAY
            )

            if (damager is Arrow) {
                val arrow: Arrow = damager
                if (arrow.shooter !is Player) return
                val shooter: Player = arrow.shooter as Player
                shooter.sendMessage(message)
                return
            }

            damager.sendMessage(message)
        }
    }
}