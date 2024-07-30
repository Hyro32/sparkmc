package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import one.hyro.builder.inventory.CustomItem
import one.hyro.builder.inventory.Menu
import one.hyro.registry.Kit
import one.hyro.registry.Queue
import org.bukkit.Material
import org.bukkit.entity.Player

object JoinCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player || args.isEmpty()) return
        val player: Player = stack.executor as Player
        val queue: Queue = Queue.instance

        val joinInventory: Menu = Menu()
            .title(Component.text("Duels"))

        val classic = CustomItem(Material.DIAMOND_CHESTPLATE)
            .displayName(Component.text("Classic"))
            .lore(Component.text("Play a classic game of duels"))

        val updated = CustomItem(Material.SHIELD)
            .displayName(Component.text("Updated"))
            .lore(Component.text("Play a game of duels with shields and crossbows"))

        when (args[0].lowercase()) {
            "singles" -> {
                classic.click { p, m -> queue.addToQueue(p.uniqueId, Kit.CLASSIC, false) }
                classic.amount(queue.getQueueByKit(Kit.CLASSIC, false).size)
                classic.build()

                updated.click { p, m -> queue.addToQueue(p.uniqueId, Kit.UPDATED, false) }
                updated.amount(queue.getQueueByKit(Kit.UPDATED, false).size)
                updated.build()
            }
            "doubles" -> {
                classic.click { p, m -> queue.addToQueue(p.uniqueId, Kit.CLASSIC, true) }
                classic.amount(queue.getQueueByKit(Kit.CLASSIC, true).size)
                classic.build()

                updated.click { p, m -> queue.addToQueue(p.uniqueId, Kit.UPDATED, true) }
                updated.amount(queue.getQueueByKit(Kit.UPDATED, true).size)
                updated.build()
            }
            else -> return
        }

        joinInventory.item(10, classic).build()
        joinInventory.item(11, updated).build()
        player.openInventory(joinInventory.inventory)
    }
}