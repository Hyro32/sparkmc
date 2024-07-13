package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import org.bukkit.entity.Player

object GamemodeCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player) return
        val player: Player = stack.executor as Player
        if (!player.hasPermission("hyro.gamemode")) return
        val gamemode: String = args.getOrNull(0) ?: return

        gamemode.let {
            when (it) {
                "survival", "0" -> player.gameMode = GameMode.SURVIVAL
                "creative", "1" -> player.gameMode = GameMode.CREATIVE
                "adventure", "2" -> player.gameMode = GameMode.ADVENTURE
                "spectator", "3" -> player.gameMode = GameMode.SPECTATOR
                else -> return player.sendMessage(Component.text("Invalid gamemode.", NamedTextColor.RED))
            }

            val message: Component = Component.text("Your gamemode has been updated to ", NamedTextColor.GREEN)
                .append(Component.text(player.gameMode.name.lowercase(), NamedTextColor.DARK_GREEN))
                .append(Component.text(".", NamedTextColor.GREEN))

            player.sendMessage(message)
        }
    }
}