package me.syari.sec_story.paper.library.inventory

import me.syari.sec_story.paper.library.event.CustomEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class OriginInventoryOpenEvent(val player: Player, val inventory: Inventory): CustomEvent()