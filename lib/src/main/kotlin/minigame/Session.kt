package one.hyro.minigame

import net.kyori.adventure.text.Component
import one.hyro.registry.SessionsRegistry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

abstract class Session(
    private val minigame: Minigame,
    val minPlayers: Int,
    val maxPlayers: Int
) {
    val playersUuids: MutableList<UUID> = mutableListOf()
    val map = SessionMap()

    var state: SessionState = SessionState.WAITING
        set(newState) {
            field = newState
            when (field) {
                SessionState.WAITING -> minigame.waiting(this)
                SessionState.STARTING -> minigame.starting(this)
                SessionState.PLAYING -> minigame.playing(this)
                SessionState.ENDING -> minigame.ending(this)
            }
        }

    init {
        require(minPlayers <= maxPlayers) { "Min players cannot be greater than max players." }
        SessionsRegistry.register(this)
        minigame.waiting(this)
    }

    fun addPlayer(uuid: UUID, component: Component) {
        check(state != SessionState.WAITING) { "Cannot join a session that is not waiting." }
        check(playersUuids.size < maxPlayers) { "Cannot join a session that is full." }

        val sessionMapSpawn: Location = map.world.spawnLocation
        Bukkit.getPlayer(uuid)?.teleportAsync(sessionMapSpawn)?.thenAccept { success ->
            if (!success) return@thenAccept
            playersUuids.add(uuid)

            for (playerUuid in playersUuids) {
                val player: Player? = Bukkit.getPlayer(playerUuid)
                player?.sendMessage(component)
            }

            if (playersUuids.size >= minPlayers) state = SessionState.STARTING
        }
    }

    fun removePlayer(uuid: UUID, component: Component) {
        val worldSpawn: Location = Bukkit.getWorld("world")?.spawnLocation ?: error("World not found.")
        Bukkit.getPlayer(uuid)?.teleportAsync(worldSpawn)
        playersUuids.remove(uuid)

        for (playerUuid in playersUuids) {
            val player: Player? = Bukkit.getPlayer(playerUuid)
            player?.sendMessage(component)
        }

        when {
            playersUuids.isEmpty() -> state = SessionState.ENDING
            state == SessionState.STARTING && playersUuids.size < minPlayers -> state = SessionState.WAITING
        }
    }

    fun broadcast(component: Component) {
        playersUuids.forEach { uuid ->
            val player: Player? = Bukkit.getPlayer(uuid)
            player?.sendMessage(component)
        }
    }

    fun isPlayerInSession(uuid: UUID): Boolean = playersUuids.contains(uuid)
}