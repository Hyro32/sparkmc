package one.hyro.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object ReplyCommand {
    fun createReplyCommand(proxy: ProxyServer): BrigadierCommand {
        val node: LiteralCommandNode<CommandSource> = BrigadierCommand.literalArgumentBuilder("reply")
            //.requires { source -> source.hasPermission("hyro.reply") }
            .executes { context ->
                val source: CommandSource = context.source
                source.sendMessage(Component.translatable("context.error.specifyMessage", NamedTextColor.RED))
                return@executes 0
            }
            .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                .executes { context ->
                    val source: Player = context.source as Player
                    val message = StringArgumentType.getString(context, "message")
                    val queue = MsgCommand.messageQueue

                    if (queue.containsKey(source.uniqueId)) {
                        val target = proxy.getPlayer(queue[source.uniqueId]!!).get()

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

                        queue.remove(source.uniqueId)
                    } else {
                        source.sendMessage(Component.translatable("context.error.noAvailableReply", NamedTextColor.RED))
                    }
                    return@executes 1
                }
            )
            .build()

        return BrigadierCommand(node)
    }
}