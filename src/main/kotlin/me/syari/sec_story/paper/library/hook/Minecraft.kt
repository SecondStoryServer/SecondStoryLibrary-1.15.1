package me.syari.sec_story.paper.library.hook

import me.syari.sec_story.paper.library.item.CustomItemStack
import org.bukkit.Material

object Minecraft {
    fun getItemFromMineCraft(id: String): CustomItemStack? {
        val material = Material.getMaterial(id.toUpperCase()) ?: return null
        return CustomItemStack(material, null)
    }
}