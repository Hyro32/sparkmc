package one.hyro.common.listener

import one.hyro.builder.inventory.CustomItem
import one.hyro.registry.InventoryRegistry
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object PlayerInteractCommonListener: Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player: Player = event.player
        val item: ItemStack = player.inventory.itemInMainHand

        val customItem: CustomItem? = InventoryRegistry.getItem(item)
        if (event.action.isRightClick && customItem != null) customItem.interactionConsumer?.invoke(player)
    }
}