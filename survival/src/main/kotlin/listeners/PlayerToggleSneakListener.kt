package one.hyro.listeners

import one.hyro.recipes.Elevator
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent

object PlayerToggleSneakListener: Listener {
    @EventHandler
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        val player: Player = event.player
        val block: Block = player.world.getBlockAt(player.location.blockX, player.location.blockY - 1, player.location.blockZ)
        val elevator: Elevator = Elevator()

        if (elevator.isElevatorBlock(block)) {
            Elevator().goToBelowFloor(player)
            return
        }
    }
}