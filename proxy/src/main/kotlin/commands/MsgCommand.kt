package one.hyro.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.Proxy
import java.util.UUID
import java.util.concurrent.TimeUnit

object MsgCommand {
    val messageQueue: MutableMap<UUID, UUID> = mutableMapOf()

    fun createMsgCommand(proxy: ProxyServer): BrigadierCommand {
        val node: LiteralCommandNode<CommandSource> = BrigadierCommand.literalArgumentBuilder("msg")
            //.requires { source -> source.hasPermission("hyro.msg") }
            .executes { context ->
                val source: CommandSource = context.source
                source.sendMessage(Component.text("You need to specify a player to message!", NamedTextColor.RED))
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
                        source.sendMessage(Component.text("You need to specify a message to send!", NamedTextColor.RED))
                    } else {
                        source.sendMessage(Component.text("Player $player is not online!", NamedTextColor.RED))
                    }
                    1
                }
                .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                    .executes { context ->
                        val source: Player = context.source as Player
                        val player = StringArgumentType.getString(context, "player")
                        val target: Player = proxy.getPlayer(player).get()
                        val message = StringArgumentType.getString(context, "message")

                        messageQueue[target.uniqueId] = source.uniqueId
                        source.sendMessage(Component.text("You -> $player: $message", NamedTextColor.GRAY))
                        target.sendMessage(Component.text("${source.username} -> You: $message", NamedTextColor.GRAY))

                        proxy.scheduler.buildTask(Proxy.instance, Runnable {
                            messageQueue.remove(target.uniqueId)
                        }).delay(30, TimeUnit.SECONDS).schedule()
                        1
                    }
                )
            )
            .build()

        return BrigadierCommand(node)
    }
}