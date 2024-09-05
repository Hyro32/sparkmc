package one.hyro.kits

import one.hyro.builder.inventory.CustomItem
import one.hyro.common.Kit
import org.bukkit.Material

object UpdatedKit: Kit {
    override fun helmet(): CustomItem {
        return CustomItem(Material.IRON_HELMET).build()
    }

    override fun chestplate(): CustomItem {
        return CustomItem(Material.IRON_CHESTPLATE).build()
    }

    override fun leggings(): CustomItem {
        return CustomItem(Material.IRON_LEGGINGS).build()
    }

    override fun boots(): CustomItem {
        return CustomItem(Material.IRON_BOOTS).build()
    }

    override fun shield(): Boolean {
        return true
    }

    override fun items(): List<CustomItem> {
        val axe = CustomItem(Material.IRON_AXE).build()
        val crossbow = CustomItem(Material.CROSSBOW).build()
        val blocks = CustomItem(Material.OAK_PLANKS).amount(32).build()
        val arrows = CustomItem(Material.ARROW).amount(10).build()
        return listOf(axe, crossbow, blocks, arrows)
    }
}