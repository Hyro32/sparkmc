package one.hyro.common.listener

import one.hyro.builder.inventory.CustomItem
import one.hyro.builder.inventory.Menu
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object InventoryClickCommonListener: Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory: Inventory = event.inventory
        val player: Player = event.whoClicked as Player
        val slot: Int = event.slot

        if (inventory.holder is Menu) {
            val menu: Menu = inventory.holder as Menu

            if (menu.clickable.not()) {
                event.isCancelled = true
                handleClick(player, menu, slot)
                return
            }

            if (menu.items.containsKey(event.slot)) {
                event.isCancelled = true
                handleClick(player, menu, slot)
            }
        }
    }

    private fun handleClick(player: Player, menu: Menu, slot: Int) {
        val item: CustomItem = menu.items[slot]!!
        item.let { it.clickConsumer?.invoke(player, menu) }
    }
}