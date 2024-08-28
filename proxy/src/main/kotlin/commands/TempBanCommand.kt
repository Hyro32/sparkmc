package one.hyro.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

object TempBanCommand {
    private val banReasons = mapOf(
        "Hacking" to "moderation.reasons.hacking",
        "Inappropriate" to "moderation.reasons.inappropriate",
        "Other" to "moderation.reasons.other",
        "Spamming" to "moderation.reasons.spamming",
        "Toxicity" to "moderation.reasons.toxicity"
    )

    fun createTempBanCommand(proxyServer: ProxyServer): BrigadierCommand {
        val tempBanCommandNode: LiteralCommandNode<CommandSource> = BrigadierCommand.literalArgumentBuilder("tempban")
            .requires { source -> source.hasPermission("hyro.tempban") }
            .executes { context ->
                val commandSource: CommandSource = context.source
                commandSource.sendMessage(Component.translatable("context.error.specifyPlayer", NamedTextColor.RED))
                return@executes 0
            }
            .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                .suggests { _, builder ->
                    val onlinePlayers = proxyServer.allPlayers.map { it.username }
                    onlinePlayers.forEach { builder.suggest(it) }
                    builder.buildFuture()
                }
                .executes { context ->
                    val commandSource: CommandSource = context.source
                    commandSource.sendMessage(Component.translatable("context.error.specifyTime", NamedTextColor.RED))
                    return@executes 1
                }
                .then(BrigadierCommand.requiredArgumentBuilder("time", StringArgumentType.word())
                    .executes { context ->
                        val commandSource: CommandSource = context.source
                        commandSource.sendMessage(Component.translatable("context.error.specifyReason", NamedTextColor.RED))
                        return@executes 1
                    }
                    .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.word())
                        .suggests { _, builder ->
                            banReasons.forEach { (reason, _) -> builder.suggest(reason) }
                            builder.buildFuture()
                        }
                        .executes { context ->
                            val commandSource: CommandSource = context.source
                            val playerName = StringArgumentType.getString(context, "player")
                            val timeArg = StringArgumentType.getString(context, "time")
                            val reason = StringArgumentType.getString(context, "reason")
                            val targetPlayer = proxyServer.getPlayer(playerName)

                            if (reason !in banReasons.keys) {
                                commandSource.sendMessage(Component.translatable("context.errors.invalidReason", NamedTextColor.RED))
                                return@executes 1
                            }

                            if (targetPlayer.isPresent) {
                                val expirationDate = parseDuration(timeArg)
                                if (expirationDate != null) tempBanPlayer(targetPlayer.get(), commandSource, expirationDate, reason)
                                else commandSource.sendMessage(Component.translatable("context.errors.invalidTimeFormat", NamedTextColor.RED))
                            } else {
                                commandSource.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED))
                            }
                            return@executes 1
                        }
                    )
                )
            )
            .build()

        return BrigadierCommand(tempBanCommandNode)
    }

    private fun parseDuration(timeArg: String): Date? {
        val regex = Regex("(\\d+)([dhms])")
        val matchResult = regex.matchEntire(timeArg) ?: return null
        val (amount, unit) = matchResult.destructured
        val timeAmount = amount.toInt()

        val calendar = Calendar.getInstance()
        when (unit) {
            "d" -> calendar.add(Calendar.DAY_OF_MONTH, timeAmount)
            "h" -> calendar.add(Calendar.HOUR, timeAmount)
            "m" -> calendar.add(Calendar.MINUTE, timeAmount)
            "s" -> calendar.add(Calendar.SECOND, timeAmount)
            else -> return null
        }
        return calendar.time
    }

    private fun tempBanPlayer(player: Player, commandSource: CommandSource, expirationDate: Date, reason: String) {
        // TODO: Add ban to database
        player.disconnect(Component.translatable("context.success.banMessage", NamedTextColor.RED))
        val successMessage = Component.translatable(
            "context.success.ban",
            Component.text(player.username, NamedTextColor.DARK_GREEN)
        ).color(NamedTextColor.GREEN)
        commandSource.sendMessage(successMessage)
    }
}

