package one.hyro.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object PlayerCommandPreprocessListener: Listener {
    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val message: String = event.message

        val blockedCommands: List<String> = listOf(
            "?",
            "help",
            "pl",
            "plugins",
            "ver",
            "version",
            "bukkit:pl",
            "bukkit:plugins",
            "bukkit:ver",
            "bukkit:version",
            "minecraft:pl",
            "minecraft:plugins",
            "minecraft:ver",
            "minecraft:version",
            "bukkit:help",
            "minecraft:help",
            "bukkit:?",
            "minecraft:?",
            "bukkit:help",
            "minecraft:help",
            "bukkit:help",
            "minecraft:help",
        )

        blockedCommands.forEach { command -> if (message.startsWith("/$command".lowercase())) event.isCancelled = true }
    }
}