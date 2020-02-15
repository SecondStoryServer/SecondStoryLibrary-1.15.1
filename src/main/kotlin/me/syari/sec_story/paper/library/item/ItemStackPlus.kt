package me.syari.sec_story.paper.library.item

import me.syari.sec_story.paper.library.Main
import me.syari.sec_story.paper.library.code.StringEditor.toColor
import me.syari.sec_story.paper.library.scheduler.CustomScheduler
import me.syari.sec_story.paper.library.scheduler.CustomScheduler.runLater
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemStackPlus {
    fun HumanEntity.giveOrDrop(items: Iterable<ItemStack?>){
        val loc = location
        items.forEach { item ->
            if(item == null) return@forEach
            if(inventory.firstEmpty() in 0 until 36) {
                inventory.addItem(item.clone())
            } else {
                val d = loc.world.dropItem(loc, item.clone())
                d.customName = "&a$name".toColor
                d.isCustomNameVisible = true
                runLater(Main.plugin, 20) {
                    d.isCustomNameVisible = false
                }
            }
        }
    }

    fun HumanEntity.giveOrDrop(items: Array<ItemStack?>){
        giveOrDrop(items.toList())
    }

    fun HumanEntity.giveOrDrop(vararg items: CustomItemStack){
        items.forEach { cItem ->
            giveOrDrop(cItem.toItemStack)
        }
    }
}