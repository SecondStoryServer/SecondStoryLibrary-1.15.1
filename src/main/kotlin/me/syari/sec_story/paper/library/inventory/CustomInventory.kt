package me.syari.sec_story.paper.library.inventory

import me.syari.sec_story.paper.library.inventory.CreateInventory.menuPlayer
import me.syari.sec_story.paper.library.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class CustomInventory(val inventory: Inventory, var id: String) {
    val events = mutableMapOf<Pair<Int, ClickType?>, () -> Unit>()
    var cancel = true
    var onEvent: ((InventoryEvent) -> Unit)? = null
    var onClick: ((InventoryClickEvent) -> Unit)? = null
    var onClose: ((InventoryCloseEvent) -> Unit)? = null
    var contents: Array<ItemStack>
        set(value) {
            inventory.contents = value
        }
        get() = inventory.contents

    private val firstEmpty get() = inventory.firstEmpty()

    inline fun with(run: CustomInventory.() -> Unit): CustomInventory{
        run.invoke(this)
        return this
    }

    fun item(index: Int): ItemStack? {
        return if(index in 0 until inventory.size) inventory.getItem(index) else null
    }

    fun item(item: CustomItemStack): Int? {
        return item(firstEmpty, item)
    }

    fun item(vararg index: Int, material: Material) {
        val item = ItemStack(material)
        index.forEach {
            item(it, item)
        }
    }

    fun item(index: Int, item: ItemStack): Int? {
        if(index in 0 until inventory.size) {
            inventory.setItem(index, item)
            return index
        }
        return null
    }

    fun item(index: Int, item: CustomItemStack): Int? {
        return item(index, item.toOneItemStack)
    }

    fun item(index: Int, mat: Material, display: String, lore: List<String>, amount: Int = 1): Int? {
        return item(
            index, CustomItemStack(
                mat, display, *lore.toTypedArray(), amount = amount
            )
        )
    }

    fun item(
        index: Int, mat: Material, display: String, vararg lore: String, amount: Int = 1
    ): Int? {
        return item(
            index, CustomItemStack(
                mat, display, *lore, amount = amount
            )
        )
    }

    fun Int?.event(clickType: ClickType, run: () -> Unit): Int? {
        return if(this != null) {
            events[this to clickType] = run
            this
        } else {
            null
        }
    }

    fun open(player: Player): CustomInventory {
        player.openInventory(inventory)
        player.menuPlayer = InventoryPlayerData(
            id, cancel, onEvent, onClick, onClose, events
        )
        return this
    }
}