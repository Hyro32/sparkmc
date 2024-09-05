package one.hyro.common

import one.hyro.builder.inventory.CustomItem

interface Kit {
    fun items(): List<CustomItem>
    fun helmet(): CustomItem? = null
    fun chestplate(): CustomItem? = null
    fun leggings(): CustomItem? = null
    fun boots(): CustomItem? = null
    fun shield(): Boolean = false
}
