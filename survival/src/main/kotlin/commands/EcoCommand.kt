package one.hyro.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object EcoCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.executor is Player && !stack.sender.hasPermission("hyro.eco")) return

        when (args[0]) {
            "give" -> {
                val player = getPlayer(args[1], stack) ?: return
                val amount = getAmount(args[2], stack) ?: return
                val account = getAccount(args[3], stack) ?: return
                ecoGive(player, amount, account)
            }
            "take" -> {
                val player = getPlayer(args[1], stack) ?: return
                val amount = getAmount(args[2], stack) ?: return
                val account = getAccount(args[3], stack) ?: return
                ecoTake(player, amount, account)
            }
            "set" -> {
                val player = getPlayer(args[1], stack) ?: return
                val amount = getAmount(args[2], stack) ?: return
                val account = getAccount(args[3], stack) ?: return
                ecoSet(player, amount, account)
            }
            "reset" -> {
                val player = getPlayer(args[1], stack) ?: return
                val account = getAccount(args[2], stack) ?: return
                ecoReset(player, account)
            }
            else -> stack.sender.sendMessage(Component.text("Invalid subcommand!", NamedTextColor.RED))
        }
    }

    private fun getPlayer(arg: String, stack: CommandSourceStack): Player? {
        val player = Bukkit.getPlayer(arg)
        if (player == null) {
            stack.sender.sendMessage(Component.text("Player not found!", NamedTextColor.RED))
            return null
        }
        return player
    }

    private fun getAmount(arg: String, stack: CommandSourceStack): Int? {
        val amount = arg.toIntOrNull()
        if (amount == null) {
            stack.sender.sendMessage(Component.text("Invalid amount!", NamedTextColor.RED))
            return null
        }
        return amount
    }

    private fun getAccount(arg: String, stack: CommandSourceStack): String? {
        return when (arg) {
            "purse", "p" -> "purse"
            "bank", "b" -> "bank"
            else ->  {
                stack.sender.sendMessage(Component.text("Invalid account type!", NamedTextColor.RED))
                return null
            }
        }
    }

    private fun ecoGive(player: Player, amount: Int, account: String) {
        val giveMessage = Component.text("You have been given ", NamedTextColor.GREEN)
            .append(Component.text("$amount", NamedTextColor.DARK_GREEN))
            .append(Component.text(" coins to your ", NamedTextColor.GREEN))
            .append(Component.text(account.lowercase(), NamedTextColor.DARK_GREEN))
            .append(Component.text(" account!", NamedTextColor.GREEN))

        // Give the player the amount of money
        player.sendMessage(giveMessage)
    }

    private fun ecoTake(player: Player, amount: Int, account: String) {
        val giveMessage = Component.text("You have been taken ", NamedTextColor.GREEN)
            .append(Component.text("$amount", NamedTextColor.DARK_GREEN))
            .append(Component.text(" coins from your ", NamedTextColor.GREEN))
            .append(Component.text(account.lowercase(), NamedTextColor.DARK_GREEN))
            .append(Component.text(" account!", NamedTextColor.GREEN))

        // Take the player the amount of money and check if they have enough
        // Players cant have negative balance
        player.sendMessage(giveMessage)
    }

    private fun ecoSet(player: Player, amount: Int, account: String) {
        val giveMessage = Component.text("Your ", NamedTextColor.GREEN)
            .append(Component.text(account.lowercase(), NamedTextColor.DARK_GREEN))
            .append(Component.text(" account has been set to ", NamedTextColor.GREEN))
            .append(Component.text("$amount", NamedTextColor.DARK_GREEN))
            .append(Component.text(" coins!", NamedTextColor.GREEN))

        // Set the player the amount of money
        player.sendMessage(giveMessage)
    }

    private fun ecoReset(player: Player, account: String) {
        val resetMessage = Component.text("Your ", NamedTextColor.GREEN)
            .append(Component.text(account.lowercase(), NamedTextColor.DARK_GREEN))
            .append(Component.text(" balance has been reset!", NamedTextColor.GREEN))

        // Reset the player's balance
        player.sendMessage(resetMessage)
    }

    override fun suggest(commandSourceStack: CommandSourceStack, args: Array<out String>): MutableCollection<String> {
        if (args.isEmpty()) return mutableListOf("give", "take", "set", "reset")
        if (args.size == 1) {
            val players = Bukkit.getOnlinePlayers().map { it.name }
            return players.toMutableList()
        }
        if (args.size == 2) {
            if (args[0] == "reset") return mutableListOf("purse", "bank")
            return mutableListOf("100", "500", "1000", "5000")
        }
        if (args.size == 3) return mutableListOf("purse", "bank")
        return mutableListOf()
    }
}