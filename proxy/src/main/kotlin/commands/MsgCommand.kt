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

                    if (target.isPresent) source.sendMessage(Component.translatable("context.error.specifyMessage", NamedTextColor.RED))
                    else source.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED))
                    return@executes 1
                }
                .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                    .executes { context ->
                        val source: Player = context.source as Player
                        val player = StringArgumentType.getString(context, "player")
                        val target: Player = proxy.getPlayer(player).get()
                        val message = StringArgumentType.getString(context, "message")

                        messageQueue[target.uniqueId] = source.uniqueId
                        val sourceMessage = Component.translatable(
                            "context.success.msg1",
                            Component.text(target.username),
                            Component.text(message)
                        ).color(NamedTextColor.GRAY)
                        source.sendMessage(sourceMessage)

                        val targetMessage = Component.translatable(
                            "context.success.msg2",
                            Component.text(source.username),
                            Component.text(message)
                        ).color(NamedTextColor.GRAY)
                        target.sendMessage(targetMessage)

                        proxy.scheduler.buildTask(Proxy.instance, Runnable {
                            messageQueue.remove(target.uniqueId)
                        }).delay(30, TimeUnit.SECONDS).schedule()
                        return@executes 1
                    }
                )
            )
            .build()

        return BrigadierCommand(node)
    }
}