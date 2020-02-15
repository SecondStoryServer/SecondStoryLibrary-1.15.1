package me.syari.sec_story.paper.library.config.content

import me.syari.sec_story.paper.library.Main.Companion.plugin
import me.syari.sec_story.paper.library.item.CustomItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

class ConfigContents {
    private val contents = mutableListOf<ConfigContent>()

    fun addContent(c: ConfigContent) {
        contents.add(c)
    }

    fun addContentsToPlayer(p: Player) {
        contents.forEach { c ->
            if(c is ConfigContentAdd) c.add(p)
        }
    }

    fun removeContentsFromPlayer(p: Player) {
        contents.forEach { c ->
            if(c is ConfigContentRemove) c.remove(p)
        }
    }

    fun getContents() = contents

    fun hasContents(p: Player): Boolean {
        fun Inventory.rem(cItem: CustomItemStack): Boolean {
            val item = cItem.toOneItemStack
            var count = item.amount
            forEach { f ->
                if(f != null &&  f.isSimilar(item)) {
                    val a = f.amount
                    if(a < count) {
                        count -= a
                        f.amount = 0
                    } else {
                        f.amount = a - count
                        return true
                    }
                }
            }
            return false
        }

        val inv = plugin.server.createInventory(null, InventoryType.PLAYER)
        inv.contents = p.inventory.contents
        contents.forEach { c ->
            if(c is ConfigContentRemove) {
                if(c is ConfigItemStack) {
                    if(!inv.rem(c.item)) return false
                } else {
                    if(! c.has(p)) return false
                }
            }
        }
        return true
    }

    val isEmpty get() = contents.all { it is ConfigContentError }
}