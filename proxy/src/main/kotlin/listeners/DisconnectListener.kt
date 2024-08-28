package one.hyro.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent

object DisconnectListener {
    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        val playerUuid = event.player.uniqueId
        // Log the player's disconnect reason
    }
}