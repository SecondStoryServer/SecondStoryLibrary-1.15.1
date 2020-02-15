package me.syari.sec_story.paper.library.message

import me.syari.sec_story.paper.library.Main.Companion.plugin
import me.syari.sec_story.paper.library.code.StringEditor.toColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SendMessage {
    fun broadcast(msg: String, sendConsole: Boolean = true) {
        broadcast(JsonBuilder(msg), sendConsole)
    }

    fun broadcast(builder: JsonBuilder, sendConsole: Boolean = true) {
        val component = builder.toTextComponent
        plugin.server.onlinePlayers.forEach { p ->
            p.sendMessage(component)
        }
        if(sendConsole) plugin.server.consoleSender.sendMessage(component)
    }

    fun send(msg: String, vararg to: CommandSender){
        val color = msg.toColor
        to.forEach {
            it.sendMessage(color)
        }
    }

    fun CommandSender.send(msg: String) {
        sendMessage(msg.toColor)
    }

    fun CommandSender.send(msg: StringBuilder) {
        send(msg.toString())
    }

    fun CommandSender.send(builder: JsonBuilder) {
        sendMessage(builder.toTextComponent)
    }

    fun sendConsole(msg: String) {
        plugin.server.consoleSender.send(msg)
    }

    fun Player.title(main: String, sub: String, fadein: Int, stay: Int, fadeout: Int) {
        sendTitle(main.toColor, sub.toColor, fadein, stay, fadeout)
    }

    fun Player.action(msg: String) {
        sendActionBar(msg.toColor)
    }
}