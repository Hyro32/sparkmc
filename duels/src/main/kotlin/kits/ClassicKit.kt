package one.hyro.kits

import net.kyori.adventure.text.Component
import one.hyro.builder.inventory.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

object ClassicKit: Kit {
    override fun icon(): CustomItem {
        return CustomItem(Material.DIAMOND_CHESTPLATE)
            .displayName(Component.text("Classic"))
            .lore(Component.text("Classic"))
            .build()
    }

    override fun helmet(): CustomItem {
        return CustomItem(Material.DIAMOND_HELMET)
            .enchantment(Enchantment.PROTECTION, 1)
            .build()
    }

    override fun chestplate(): CustomItem {
        return CustomItem(Material.DIAMOND_CHESTPLATE)
            .enchantment(Enchantment.PROTECTION, 1)
            .build()
    }

    override fun leggings(): CustomItem {
        return CustomItem(Material.DIAMOND_LEGGINGS)
            .enchantment(Enchantment.PROTECTION, 1)
            .build()
    }

    override fun boots(): CustomItem {
        return CustomItem(Material.DIAMOND_BOOTS)
            .enchantment(Enchantment.PROTECTION, 1)
            .build()
    }

    override fun items(): List<CustomItem> {
        val sword = CustomItem(Material.DIAMOND_SWORD).enchantment(Enchantment.SHARPNESS, 2).build()
        val rod = CustomItem(Material.FISHING_ROD).build()
        val woodenBlocks = CustomItem(Material.OAK_PLANKS).amount(32).build()
        val apples = CustomItem(Material.GOLDEN_APPLE).amount(50).build()
        return listOf(sword, rod, woodenBlocks, apples)
    }
}