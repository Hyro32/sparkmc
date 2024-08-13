package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import one.hyro.builder.inventory.CustomItem
import one.hyro.builder.inventory.Menu
import one.hyro.kits.Kit
import one.hyro.Queue
import one.hyro.kits.ClassicKit
import org.bukkit.Material
import org.bukkit.entity.Player

object JoinCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player || args.isEmpty()) return
        val player: Player = stack.executor as Player

        val joinInventory: Menu = Menu().title(Component.text("Duels"))
        val classic: CustomItem = ClassicKit.icon()

        when (args[0].lowercase()) {
            "singles" -> {
                classic.click { p, m -> Queue.addToQueue(p.uniqueId, ClassicKit, false) }
                classic.amount(Queue.getQueueByKit(ClassicKit, false).size)
                classic.build()
            }
            "doubles" -> {
                classic.click { p, m -> Queue.addToQueue(p.uniqueId, ClassicKit, true) }
                classic.amount(Queue.getQueueByKit(ClassicKit, true).size)
                classic.build()
            }
            else -> return
        }

        joinInventory.item(10, classic).build()
        player.openInventory(joinInventory.inventory)
    }

    override fun suggest(commandSourceStack: CommandSourceStack, args: Array<out String>): MutableCollection<String> {
        return mutableSetOf("singles", "doubles")
    }
}