package me.syari.sec_story.paper.library.config.content

import me.syari.sec_story.paper.library.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

class ConfigExp(val value: Int): ConfigContentAdd, ConfigContentRemove {
    override fun add(p: Player) {
        p.giveExp(value)
    }

    override fun remove(p: Player) {
        p.totalExperience -= value
    }

    override fun has(p: Player): Boolean {
        return value <= p.totalExperience
    }

    override fun display(p: Player) = CustomItemStack(
        Material.EXPERIENCE_BOTTLE,
        "&d${String.format("%,d", value)}Exp",
        "&aプレイヤーが持っている経験値",
        "&d現在の経験値: ${String.format("%,d", p.totalExperience)}"
    )
}