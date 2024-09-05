package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.minigame.Session
import one.hyro.Queue
import one.hyro.registry.SessionsRegistry
import org.bukkit.entity.Player

object LeaveCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val player: Player = stack.sender as Player

        if (Queue.isPlayerQueued(player.uniqueId)) {
            Queue.removePlayerFromQueue(player.uniqueId)
            player.sendMessage(Component.text("You left the queue!", NamedTextColor.RED))
        }

        if (SessionsRegistry.isPlayerInAnySession(player.uniqueId)) {
            val session: Session = SessionsRegistry.getSession(player.uniqueId)!!
            session.removePlayer(player.uniqueId, Component.text("You left the session!", NamedTextColor.RED))
        }
    }
}