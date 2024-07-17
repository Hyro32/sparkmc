package one.hyro.listeners

import one.hyro.recipes.Elevator
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

object BlockBreakListener: Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player: Player = event.player
        val block: Block = event.block
        val elevator = Elevator()

        if (elevator.isElevatorBlock(block)) Elevator().dropElevatorByColor(player, block, block.type)
    }
}