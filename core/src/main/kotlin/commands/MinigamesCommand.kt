package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import one.hyro.builder.inventory.CustomItem
import one.hyro.builder.inventory.Menu
import org.bukkit.Material
import org.bukkit.entity.Player

object MinigamesCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor !is Player) return

        val survival: CustomItem = CustomItem(Material.GRASS_BLOCK)
            .displayName(Component.text("Survival"))
            .lore(Component.text("Survive in a world with limited resources"))
            .click { player: Player ->
                player.sendMessage(Component.text("You clicked on the survival minigame!"))
            }
            .build()

        val duels: CustomItem = CustomItem(Material.DIAMOND_SWORD)
            .displayName(Component.text("Duels"))
            .lore(Component.text("Fight against other players"))
            .click { player: Player ->
                player.sendMessage(Component.text("You clicked on the duels minigame!"))
            }

        val minigames: Menu = Menu()
            .title(Component.text("Minigames"))
            .item(12, survival)
            .item(14, duels)
            .build()

        val player: Player = stack.executor as Player
        player.openInventory(minigames.inventory)
    }
}