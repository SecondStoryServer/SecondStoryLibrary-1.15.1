package me.syari.sec_story.paper.library.event.sign

import me.syari.sec_story.paper.library.event.CustomEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SignClickEvent(val player: Player, val sign: CustomSign, val action: Action, val item: ItemStack?): CustomEvent(){
    enum class Action {
        Right,
        Left;

        val isRight get() = this == Right
        val isLeft get() = this == Left
    }

    var isBreakCancel = false
}