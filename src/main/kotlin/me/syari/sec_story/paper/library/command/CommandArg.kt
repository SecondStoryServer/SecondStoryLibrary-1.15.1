package me.syari.sec_story.paper.library.command

import org.bukkit.Bukkit.getOfflinePlayer
import org.bukkit.Bukkit.getPlayer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class CommandArg(private val array: Array<out String>, private val message: CommandMessage) {
    operator fun get(index: Int) = array[index]

    fun getOrNull(index: Int) = array.getOrNull(index)

    fun whenIndex(index: Int) = getOrNull(index)?.toLowerCase()

    val size get() = array.size

    val isEmpty get() = size == 0

    val isNotEmpty get() = size != 0

    val toList by lazy { array.toList() }

    fun joinToString(separator: CharSequence = ", ") = array.joinToString(separator)

    fun slice(first: Int) = slice(first until size)

    private fun slice(range: IntRange) = array.slice(range)

    fun getOfflinePlayer(index: Int, equalName: Boolean): OfflinePlayer? {
        val rawPlayer = getOrNull(index)
        if(rawPlayer == null){
            message.errorNotEnterPlayer()
            return null
        }
        val player = getOfflinePlayer(rawPlayer)
        return if (!equalName || player.name?.toLowerCase() == rawPlayer.toLowerCase()) {
            player
        } else {
            message.errorNotFoundPlayer()
            null
        }
    }

    fun getPlayer(index: Int, equalName: Boolean): Player? {
        val rawPlayer = getOrNull(index)
        if(rawPlayer == null){
            message.errorNotEnterPlayer()
            return null
        }
        val player = getPlayer(rawPlayer)
        return if (player != null && (!equalName || player.name.toLowerCase() == rawPlayer.toLowerCase())) {
            player
        } else {
            message.errorNotFoundPlayer()
            null
        }
    }
}