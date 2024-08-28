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
                source.sendMessage(Component.text("You need to specify a player to kick!", NamedTextColor.RED))
                0
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
                        target.get().disconnect(Component.text("You have been kicked from the server!", NamedTextColor.RED))
                        source.sendMessage(Component.text("Successfully kicked $player!", NamedTextColor.GREEN))
                    } else {
                        source.sendMessage(Component.text("Player $player is not online!", NamedTextColor.RED))
                    }
                    1
                }
            )
            .build()

        return BrigadierCommand(node)
    }
}