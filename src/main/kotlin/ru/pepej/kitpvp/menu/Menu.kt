package ru.pepej.kitpvp.menu

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.pepej.kitpvp.utils.item
import ru.pepej.kitpvp.utils.unaryPlus

abstract class Menu(
    private var menuName: String,
    private var slots: Int,
    private var playerMenuUtility: PlayerMenuUtility
) :
    InventoryHolder {
    private lateinit var inventory: Inventory
    override fun getInventory(): Inventory {
        return inventory
    }

    private var fill = item(Material.matchMaterial("160"),1,7) { displayName = +"&c" }

    abstract fun handleMenu(e: InventoryClickEvent)
    abstract fun setMenuItems()
    fun open() {
        inventory = Bukkit.createInventory(this, slots, +menuName)
        setMenuItems()
        setFillerGlass()
        playerMenuUtility.owner.openInventory(inventory)

    }

    private fun setFillerGlass() {
        inventory.contents.forEachIndexed { index, value ->
           if(value == null) {
               inventory.setItem(index, fill)
           }
        }
    }

}