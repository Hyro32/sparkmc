package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import one.hyro.Queue
import one.hyro.builder.inventory.CustomItem
import one.hyro.builder.inventory.Menu
import one.hyro.kits.ClassicKit
import one.hyro.kits.UpdatedKit
import org.bukkit.Material
import org.bukkit.entity.Player

object JoinCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player || args.isEmpty()) return

        val duelsMenu = Menu().title(Component.text("Duels")).size(9)
        val classic = CustomItem(Material.DIAMOND_CHESTPLATE).displayName(Component.text("Classic Kit"))
        val updated = CustomItem(Material.CROSSBOW).displayName(Component.text("Updated Kit"))

        when (args[0].lowercase()) {
            "singles" -> {
                classic
                    .amount(Queue.getQueueByKit(ClassicKit, false).size)
                    .click { player: Player -> Queue.addToQueue(player.uniqueId, ClassicKit, false) }
                    .build()

                updated
                    .amount(Queue.getQueueByKit(UpdatedKit, false).size)
                    .click { player: Player -> Queue.addToQueue(player.uniqueId, UpdatedKit, false) }
                    .build()

                duelsMenu.item(0, classic)
                duelsMenu.item(1, updated)
            }
            "doubles" -> {
                classic
                    .amount(Queue.getQueueByKit(ClassicKit, true).size)
                    .click { player: Player -> Queue.addToQueue(player.uniqueId, ClassicKit, true) }
                    .build()

                updated
                    .amount(Queue.getQueueByKit(UpdatedKit, false).size)
                    .click { player: Player -> Queue.addToQueue(player.uniqueId, UpdatedKit, true) }
                    .build()

                duelsMenu.item(0, classic)
                duelsMenu.item(1, updated)
            }
            else -> return
        }


        val player: Player = stack.executor as Player
        duelsMenu.build()
        player.openInventory(duelsMenu.inventory)
    }

    override fun suggest(stack: CommandSourceStack, args: Array<out String>): MutableCollection<String> {
        return mutableListOf("singles", "doubles")
    }
}