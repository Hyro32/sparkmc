package one.hyro.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import one.hyro.recipes.Elevator
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PlayerJumpListener: Listener {
    @EventHandler
    fun onPlayerJump(event: PlayerJumpEvent) {
        val player: Player = event.player
        val block: Block = player.world.getBlockAt(player.location.blockX, player.location.blockY - 1, player.location.blockZ)
        val elevator = Elevator()

        if (elevator.isElevatorBlock(block)) {
            Elevator().goToAboveFloor(player)
            return
        }
    }
}