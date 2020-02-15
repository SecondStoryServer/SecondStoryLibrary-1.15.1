package me.syari.sec_story.paper.library.message

import me.syari.sec_story.paper.library.code.StringEditor.toColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class JsonBuilder() {
    constructor(first: String): this(){
        append(first)
    }

    fun append(text: String, hover: String? = null, clickType: JsonClickType? = null, clickText: String = ""): JsonBuilder{
        message.add(JsonMessage.Text(text, hover, clickType, clickText))
        return this
    }

    fun appendln(): JsonBuilder{
        message.add(JsonMessage.NewLine)
        return this
    }

    fun appendln(text: String, hover: String? = null, clickType: JsonClickType? = null, clickText: String = ""): JsonBuilder{
        return append(text, hover, clickType, clickText).appendln()
    }

    private val message = mutableListOf<JsonMessage>()

    val toTextComponent get(): TextComponent {
        val t = TextComponent()
        message.forEach { f ->
            when(f){
                is JsonMessage.Text -> {
                    val a = TextComponent(f.text.toColor)
                    f.hover?.let {
                        a.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent(it.toColor)))
                    }
                    f.clickType?.let {
                        a.clickEvent = ClickEvent(it.event, f.clickText.toColor)
                    }
                    t.addExtra(a)
                }
                is JsonMessage.NewLine -> t.addExtra("\n")
            }
        }
        return t
    }

    sealed class JsonMessage {
        class Text(val text: String, val hover: String?, val clickType: JsonClickType?, val clickText: String): JsonMessage()

        object NewLine: JsonMessage()
    }
}