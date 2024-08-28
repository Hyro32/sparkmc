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
                source.sendMessage(Component.text("You need to specify a message!", NamedTextColor.RED))
                0
            }
            .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                .executes { context ->
                    val source: Player = context.source as Player
                    val message = StringArgumentType.getString(context, "message")
                    val queue = MsgCommand.messageQueue

                    if (queue.containsKey(source.uniqueId)) {
                        val target = proxy.getPlayer(queue[source.uniqueId]!!).get()
                        source.sendMessage(Component.text("You -> ${target.username}: $message", NamedTextColor.GRAY))
                        target.sendMessage(Component.text("${source.username} -> You: $message", NamedTextColor.GRAY))
                        queue.remove(source.uniqueId)
                    } else {
                        source.sendMessage(Component.text("You have no one to reply to!", NamedTextColor.RED))
                    }
                    1
                }
            )
            .build()

        return BrigadierCommand(node)
    }
}