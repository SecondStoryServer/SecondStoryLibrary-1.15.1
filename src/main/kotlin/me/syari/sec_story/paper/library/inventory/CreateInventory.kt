package me.syari.sec_story.paper.library.inventory

import me.syari.sec_story.paper.library.Main.Companion.plugin
import me.syari.sec_story.paper.library.code.StringEditor.toColor
import me.syari.sec_story.paper.library.init.EventInit
import me.syari.sec_story.paper.library.player.UUIDPlayer
import me.syari.sec_story.paper.library.scheduler.CustomScheduler.runLater
import org.bukkit.Bukkit.createInventory
import org.bukkit.OfflinePlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory

object CreateInventory: EventInit {
    @EventHandler
    fun on(e: InventoryOpenEvent) {
        val p = e.player as Player
        val inv = e.inventory
        if(p.isOpenCustomInv) {
            CustomInventoryOpenEvent(p, inv)
        } else {
            OriginInventoryOpenEvent(p, inv)
        }.callEvent()
    }

    @EventHandler
    fun on(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val data = p.menuPlayer ?: return
        if(data.cancel) {
            e.isCancelled = true
        }
        if(e.inventory == e.clickedInventory) {
            data.runEvent(e.slot, e.click)
        }
        data.onClick(e)
        if(e.click == ClickType.MIDDLE && p.isOp) {
            if(e.currentItem != null) {
                e.isCancelled = true
                p.inventory.addItem(e.currentItem)
            }
        }
    }

    @EventHandler
    fun on(e: InventoryCloseEvent) {
        val p = e.player as Player
        val data = p.menuPlayer ?: return
        data.onClose(e)
        p.removeInv()
        runLater(plugin, 5) {
            p.updateInventory()
        }
    }

    fun inventory(id: String?, inv: Inventory): CustomInventory {
        return CustomInventory(inv, id ?: inv.type.defaultTitle)
    }

    fun inventory(display: String?, type: InventoryType): CustomInventory {
        val name = display?.toColor ?: type.defaultTitle
        return inventory(name, createInventory(null, type, name))
    }

    fun inventory(display: String?, type: InventoryType, run: CustomInventory.() -> Unit): CustomInventory {
        val c = inventory(display, type)
        c.run()
        return c
    }

    fun inventory(display: String, line: Int = 3, run: CustomInventory.() -> Unit): CustomInventory {
        val name = display.toColor
        val c = inventory(name, createInventory(null, (if(line in 1..6) line else 3) * 9, name))
        c.run()
        return c
    }

    val Player.isOpenCustomInv get() = menuPlayer != null

    private val menuPlayers = mutableMapOf<UUIDPlayer, InventoryPlayerData>()

    var OfflinePlayer.menuPlayer
        get() = menuPlayers[UUIDPlayer(this)]
        set(value) {
            val uuidPlayer = UUIDPlayer(this)
            if(value != null) {
                menuPlayers[uuidPlayer] = value
            } else {
                menuPlayers.remove(uuidPlayer)
            }
        }

    private fun Player.removeInv() {
        menuPlayer = null
    }

    fun reopenStartsWith(startsWith: String, inv: (Player) -> Unit) {
        val c = startsWith.toColor
        menuPlayers.forEach { (u, i) ->
            if(i.id.startsWith(c)) {
                val p = u.player ?: return@forEach
                inv.invoke(p)
            }
        }
    }

    fun reopen(id: String, inv: (Player) -> Unit) {
        val c = id.toColor
        menuPlayers.forEach { (u, i) ->
            if(i.id == c) {
                val p = u.player ?: return@forEach
                inv.invoke(p)
            }
        }
    }

    fun close(humanEntity: HumanEntity){
        humanEntity.closeInventory()
    }

    fun close(id: String) {
        val c = id.toColor
        menuPlayers.forEach { (u, i) ->
            if(i.id == c) {
                u.player?.closeInventory()
            }
        }
    }

    fun closeStartsWith(startsWith: String) {
        val c = startsWith.toColor
        menuPlayers.forEach { (u, i) ->
            if(i.id.startsWith(c)) {
                u.player?.closeInventory()
            }
        }
    }
}