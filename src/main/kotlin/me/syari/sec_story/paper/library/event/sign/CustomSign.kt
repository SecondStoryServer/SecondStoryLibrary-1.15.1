package me.syari.sec_story.paper.library.event.sign

import me.syari.sec_story.paper.library.code.StringEditor.toColor
import me.syari.sec_story.paper.library.persistentData.CustomPersistentData
import me.syari.sec_story.paper.library.persistentData.CustomPersistentDataContainer
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.plugin.java.JavaPlugin

class CustomSign(private val sign: Sign): CustomPersistentDataContainer {
    companion object {
        fun fromBlock(block: Block): CustomSign? {
            return (block.state as? Sign)?.let { CustomSign(it) }
        }
    }

    fun setLine(index: Int, text: String, update: Boolean = true){
        sign.setLine(index, text.toColor)
        if(update) update()
    }

    fun setLine(line: Collection<String>){
        setLine(*line.toTypedArray())
    }

    fun setLine(vararg line: String){
        line.forEachIndexed { index, text ->
            setLine(index, text, false)
        }
        update()
    }

    fun setEmpty(){
        setLine("", "", "", "")
    }

    fun update(){
        sign.update()
    }

    val location by lazy { sign.location }

    val lines by lazy { sign.lines }

    val isNotEmpty by lazy { lines.firstOrNull { it.isNotEmpty() } != null }

    val isEmpty: Boolean by lazy { !isNotEmpty }

    override fun <E> editPersistentData(plugin: JavaPlugin, run: CustomPersistentData.() -> E): E? {
        return getPersistentData(plugin, run)
    }

    override fun <E> getPersistentData(plugin: JavaPlugin, run: CustomPersistentData.() -> E): E? {
        return run.invoke(CustomPersistentData(plugin, sign.persistentDataContainer))
    }
}