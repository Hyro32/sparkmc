package one.hyro.builder.inventory

import net.kyori.adventure.text.Component
import one.hyro.scheduler.Schedule
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class Menu: InventoryHolder {
    private var _inventory: Inventory? = null
    private var title: Component = Component.text("Menu")
    private var size: Int = 27
    var items: MutableMap<Int, CustomItem> = mutableMapOf()
    var clickable: Boolean = false

    fun size(size: Int) = apply {
        this.size = size
        return this
    }

    fun title(title: Component) = apply {
        this.title = title
        return this
    }

    fun item(slot: Int, item: CustomItem) = apply {
        if (_inventory?.getItem(slot) != null) return@apply
        this.items[slot] = item
        return this
    }

    fun fillColumn(column: Int, material: Material) = apply {
        val rows = size / 9
        for (row in 0 until rows) {
            val slot = row * 9 + column
            if (_inventory?.getItem(slot) != null) continue
            val item: CustomItem = CustomItem(material).displayName(Component.empty())
            this.items[slot] = item
        }
    }

    fun fillRow(row: Int, material: Material) = apply {
        for (inventoryRow in 0 until size) {
            val slot = row * size + inventoryRow
            if (_inventory?.getItem(slot) != null) continue
            val item: CustomItem = CustomItem(material).displayName(Component.empty())
            this.items[slot] = item
        }
    }

    fun clickable() = apply {
        this.clickable = true
        return this
    }

    fun build() = apply {
        this._inventory = Bukkit.createInventory(this, size, title)
        updateMenu()
    }

    private fun updateMenu() = Schedule.runTaskTimer { items.forEach { (slot, item) -> _inventory?.setItem(slot, item.item) } }
    override fun getInventory(): Inventory = _inventory ?: throw IllegalStateException("Inventory has not been initialized yet.")
}