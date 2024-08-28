package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

object JoinCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player || args.isEmpty()) return

        when (args[0].lowercase()) {
            "singles" -> {}
            "doubles" -> {}
            else -> return
        }
    }

    override fun suggest(stack: CommandSourceStack, args: Array<out String>): MutableCollection<String> {
        return mutableSetOf("singles", "doubles")
    }
}