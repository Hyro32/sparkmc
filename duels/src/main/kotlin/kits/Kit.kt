package one.hyro.kits

import one.hyro.builder.inventory.CustomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface Kit {
    fun icon(doubles: Boolean): CustomItem
    fun items(): List<CustomItem>
    fun helmet(): CustomItem? = null
    fun chestplate(): CustomItem? = null
    fun leggings(): CustomItem? = null
    fun boots(): CustomItem? = null
    fun shield(): Boolean = false

    companion object {
        fun applyInventory(kit: Kit, player: Player) {
            player.inventory.clear()
            player.inventory.helmet = kit.helmet()?.item
            player.inventory.chestplate = kit.chestplate()?.item
            player.inventory.leggings = kit.leggings()?.item
            player.inventory.boots = kit.boots()?.item
            if (kit.shield()) player.inventory.setItemInOffHand(ItemStack(Material.SHIELD))
            kit.items().forEach { player.inventory.addItem(it.item) }
        }
    }
}
