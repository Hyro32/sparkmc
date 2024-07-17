package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent.runCommand
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.builder.Item
import one.hyro.builder.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object TradeCommand: BasicCommand {
    private val tradeRequests: MutableMap<UUID, UUID> = mutableMapOf()

    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player || args.isEmpty()) return
        val player: Player = stack.executor as Player
        val target: Player? = Bukkit.getPlayer(args[0])

        if (player.uniqueId == target?.uniqueId) {
            player.sendMessage(Component.text("You can't trade with yourself!", NamedTextColor.RED))
            return
        }

        target?.let {
            val tradeMessage: Component = Component.text("Trade request from ${player.name}!", NamedTextColor.GRAY)
                .appendNewline()
                .append(
                    Component.text("[Click here to accept]", NamedTextColor.BLUE)
                        .clickEvent(runCommand("/trade accept"))
                )

            tradeRequests[it.uniqueId] = player.uniqueId
            it.sendMessage(tradeMessage)
        }

        when(args[0].lowercase()) {
            "accept" -> {
                val requester: Player = tradeRequests[player.uniqueId]?.let { Bukkit.getPlayer(it) } ?: return

                val acceptPlayer: Item = Item(Material.PINK_DYE)
                    .displayName(Component.text("Accept", NamedTextColor.GREEN))
                    .lore(Component.text("Click to accept the trade request!", NamedTextColor.GRAY))
                    .click { p: Player -> p.sendMessage(Component.text("The trade request has been accepted!", NamedTextColor.GREEN)) }
                    .build()

                val acceptRequester: Item = Item(Material.PINK_DYE)
                    .displayName(Component.text("Accept", NamedTextColor.GREEN))
                    .lore(Component.text("Click to accept the trade request!", NamedTextColor.GRAY))
                    .click { p: Player -> p.sendMessage(Component.text("The trade request has been accepted!", NamedTextColor.GREEN)) }
                    .build()

                val menu: Menu = Menu()
                    .title(Component.text("Trade", NamedTextColor.GRAY))
                    .size(54)
                    .item(45, acceptPlayer)
                    .item(53, acceptRequester)
                    .fillColumn(4, Material.GRAY_STAINED_GLASS_PANE)
                    .clickable()
                    .build()

                player.openInventory(menu.inventory)
                requester.openInventory(menu.inventory)
            }
            "deny" -> {
                val requester: Player = tradeRequests[player.uniqueId]?.let { Bukkit.getPlayer(it) } ?: return
                requester.sendMessage(Component.text("The trade request has been denied!", NamedTextColor.RED))
                tradeRequests.remove(player.uniqueId)
            }
            else -> return
        }
    }
}