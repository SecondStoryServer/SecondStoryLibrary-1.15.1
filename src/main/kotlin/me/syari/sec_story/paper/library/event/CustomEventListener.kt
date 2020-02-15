package me.syari.sec_story.paper.library.event

import me.syari.sec_story.paper.library.event.sign.CustomSign
import me.syari.sec_story.paper.library.event.sign.SignClickEvent
import me.syari.sec_story.paper.library.init.EventInit
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object CustomEventListener: EventInit {
    @EventHandler
    fun on(e: PlayerInteractEvent){
        val action = when(e.action){
            Action.LEFT_CLICK_BLOCK -> SignClickEvent.Action.Left
            Action.RIGHT_CLICK_BLOCK -> SignClickEvent.Action.Right
            else -> return
        }
        val sign = e.clickedBlock?.state as? Sign ?: return
        val run = SignClickEvent(e.player, CustomSign(sign), action, e.item)
        run.callEvent()
        if(run.isBreakCancel){
            e.isCancelled = true
        }
    }
}