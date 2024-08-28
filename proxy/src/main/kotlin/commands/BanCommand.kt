package one.hyro.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object BanCommand {
    private val banReasons = mapOf(
        "Hacking" to "moderation.reasons.hacking",
        "Inappropriate" to "moderation.reasons.inappropriate",
        "Other" to "moderation.reasons.other",
        "Spamming" to "moderation.reasons.spamming",
        "Toxicity" to "moderation.reasons.toxicity"
    )

    fun createBanCommand(proxyServer: ProxyServer): BrigadierCommand {
        val banCommandNode: LiteralCommandNode<CommandSource> = BrigadierCommand.literalArgumentBuilder("ban")
            .requires { source -> source.hasPermission("hyro.ban") }
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
                        val targetPlayer = proxyServer.getPlayer(playerName)
                        val reason = StringArgumentType.getString(context, "reason")

                        if (reason !in banReasons.keys) {
                            commandSource.sendMessage(Component.translatable("context.errors.invalidReason", NamedTextColor.RED))
                            return@executes 1
                        }

                        if (targetPlayer.isPresent) banPlayer(targetPlayer.get(), commandSource, reason)
                        else commandSource.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED))
                        return@executes 1
                    }
                )
            )
            .build()

        return BrigadierCommand(banCommandNode)
    }

    private fun banPlayer(player: Player, commandSource: CommandSource, reason: String) {
        // TODO: Add ban to database
        player.disconnect(Component.translatable("context.success.banMessage", NamedTextColor.RED))
        val successMessage = Component.translatable(
            "context.success.ban",
            Component.text(player.username, NamedTextColor.DARK_GREEN)
        ).color(NamedTextColor.GREEN)
        commandSource.sendMessage(successMessage)
    }
}