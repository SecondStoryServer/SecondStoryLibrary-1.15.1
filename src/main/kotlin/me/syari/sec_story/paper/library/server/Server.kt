package me.syari.sec_story.paper.library.server

import me.syari.sec_story.paper.library.Main.Companion.plugin
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

object Server {
    fun convertUUID(raw: String): UUID? {
        return if(raw.split("-").size == 5) UUID.fromString(raw) else null
    }

    fun convertUUIDUnSafe(raw: String): UUID {
        return UUID.fromString(raw)
    }

    val maxPlayers get() = plugin.server.maxPlayers
}