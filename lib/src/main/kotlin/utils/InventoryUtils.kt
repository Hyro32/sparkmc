package one.hyro.utils

import one.hyro.common.Kit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object InventoryUtils {
    fun applyInventory(player: Player, kit: Kit) {
        player.inventory.clear()
        player.inventory.helmet = kit.helmet()?.item
        player.inventory.chestplate = kit.chestplate()?.item
        player.inventory.leggings = kit.leggings()?.item
        player.inventory.boots = kit.boots()?.item
        if (kit.shield()) player.inventory.setItemInOffHand(ItemStack(Material.SHIELD))
        kit.items().forEach { player.inventory.addItem(it.item) }
    }
}