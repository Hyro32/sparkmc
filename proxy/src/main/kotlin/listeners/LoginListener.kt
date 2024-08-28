package one.hyro.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent

object LoginListener {
    @Subscribe
    fun onLogin(event: LoginEvent) {
        val playerUuid = event.player.uniqueId
        // Check if the player is banned and if so, disconnect them
    }
}