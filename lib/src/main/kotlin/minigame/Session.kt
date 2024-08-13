package one.hyro.minigame

import net.kyori.adventure.text.Component
import one.hyro.registry.SessionsRegistry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

open class Session(val minigame: Minigame, val min: Int, max: Int) {
    private val playersUuid: MutableList<UUID> = mutableListOf()
        get() = field

    private val map = SessionMap()
        get() = field

    private var state: SessionState = SessionState.WAITING
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
        if (min > max) throw IllegalArgumentException("Min players cannot be greater than max players.")
        SessionsRegistry.register(this)
        minigame.waiting(this)
    }

    fun addPlayer(uuid: UUID, component: Component) {
        if (this.state != SessionState.WAITING) throw IllegalStateException("Cannot join a session that is not waiting.")
        val player: Player? = Bukkit.getPlayer(uuid)

        player?.let {
            val location: Location = map.world.spawnLocation
            it.teleportAsync(location).thenAccept { success ->
                if (!success) return@thenAccept
                playersUuid.add(uuid)

                for (playerUuid: UUID in playersUuid) {
                    val sessionPlayer: Player? = Bukkit.getPlayer(playerUuid)
                    sessionPlayer?.sendMessage(component) ?: continue
                }

                if (playersUuid.size >= min) state = SessionState.STARTING
            }
        }
    }

    fun removePlayer(uuid: UUID, component: Component) {
        val player: Player? = Bukkit.getPlayer(uuid);

        player?.let {
            val location: Location = Bukkit.getWorld("world")?.spawnLocation ?: return@let
            it.teleportAsync(location)
        }

        for (playerUuid: UUID in playersUuid) {
            val sessionPlayer: Player = Bukkit.getPlayer(playerUuid) ?: continue
            sessionPlayer.sendMessage(component)
        }

        playersUuid.remove(uuid)
        val isStartingWithPlayers: Boolean = state == SessionState.STARTING && (playersUuid.size < min && playersUuid.isNotEmpty())
        if (isStartingWithPlayers) state = SessionState.WAITING
        if (playersUuid.isEmpty()) state = SessionState.ENDING
    }

    fun isPlayerInSession(uuid: UUID): Boolean = playersUuid.contains(uuid)
}