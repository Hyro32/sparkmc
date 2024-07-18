package one.hyro.listeners

import io.papermc.paper.event.player.PlayerPickItemEvent
import one.hyro.HyroCore
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent

object LobbyListeners: Listener {
    private val configuration: FileConfiguration = HyroCore.instance?.config!!
    private val lobbyWorlds: List<String> = configuration.getStringList("lobby.worlds")

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerDamage: Boolean = configuration.getBoolean("lobby.player-damage")
        if (!playerDamage && isLobbyWorld(event.player.world.name)) event.player.isInvulnerable = true
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        val hunger: Boolean = configuration.getBoolean("lobby.hunger")
        if (!hunger && isLobbyWorld(event.entity.world.name)) {
            event.isCancelled = true
            event.foodLevel = 20
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        val itemDrop: Boolean = configuration.getBoolean("lobby.item-drop")
        if (!itemDrop && isLobbyWorld(event.player.world.name)) event.isCancelled = true
    }

    @EventHandler
    fun onItemPickup(event: PlayerPickItemEvent) {
        val itemPickup: Boolean = configuration.getBoolean("lobby.item-pickup")
        if (!itemPickup && isLobbyWorld(event.player.world.name)) event.isCancelled = true
    }

    private fun isLobbyWorld(world: String): Boolean {
        return lobbyWorlds.contains(world)
    }
}