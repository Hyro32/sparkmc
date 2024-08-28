package one.hyro.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object KickCommand {
    fun createKickCommand(proxy: ProxyServer): BrigadierCommand {
        val node: LiteralCommandNode<CommandSource> = BrigadierCommand.literalArgumentBuilder("kick")
            .requires { source -> source.hasPermission("hyro.kick") }
            .executes { context ->
                val source: CommandSource = context.source
                source.sendMessage(Component.translatable("context.error.specifyPlayer", NamedTextColor.RED))
                return@executes 0
            }
            .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                .suggests { _, builder ->
                    val players = proxy.allPlayers.map { it.username }
                    players.forEach { builder.suggest(it) }
                    builder.buildFuture()
                }
                .executes { context ->
                    val source: CommandSource = context.source
                    val player = StringArgumentType.getString(context, "player")
                    val target = proxy.getPlayer(player)

                    if (target.isPresent) {
                        target.get().disconnect(Component.translatable("context.success.kickMessage", NamedTextColor.RED))
                        val successMessage = Component.translatable(
                            "context.success.kick",
                            Component.text(player, NamedTextColor.DARK_GREEN)
                        ).color(NamedTextColor.GREEN)
                        source.sendMessage(successMessage)
                    } else {
                        source.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED))
                    }
                    return@executes 1
                }
            )
            .build()

        return BrigadierCommand(node)
    }
}