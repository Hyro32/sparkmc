package one.hyro.builder

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class Menu: InventoryHolder {
    private var _inventory: Inventory? = null
    var title: Component = Component.text("Menu")
    var size: Int = 27
    var items: MutableMap<Int, Item> = mutableMapOf()
    var draggable: Boolean = false

    fun size(size: Int) = apply {
        this.size = size
        return this
    }

    fun title(title: Component) = apply {
        this.title = title
        return this
    }

    fun item(slot: Int, item: Item) = apply {
        if (_inventory?.getItem(slot) != null) return@apply
        this.items[slot] = item
        return this
    }

    fun draggable() = apply {
        draggable = true
        return this
    }

    fun fillColumn(column: Int, material: Material) = apply {
        for (inventoryColumn in 0 until size) {
            val slot = column + inventoryColumn * size
            if (_inventory?.getItem(slot) != null) continue
            val item: Item = Item(material).displayName(Component.empty())
            this.items[slot] = item
        }
    }

    fun fillRow(row: Int, material: Material) = apply {
        for (inventoryRow in 0 until size) {
            val slot = row * size + inventoryRow
            if (_inventory?.getItem(slot) != null) continue
            val item: Item = Item(material).displayName(Component.empty())
            this.items[slot] = item
        }
    }

    fun build() = apply {
        this._inventory = Bukkit.createInventory(this, size.toInt(), title)
        items.forEach { (slot, item) -> this._inventory?.setItem(slot, item.item) }
        return this
    }

    override fun getInventory(): Inventory = _inventory ?: throw IllegalStateException("Inventory has not been initialized yet.")
}