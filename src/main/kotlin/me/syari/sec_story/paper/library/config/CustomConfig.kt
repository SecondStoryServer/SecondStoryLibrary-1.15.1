package me.syari.sec_story.paper.library.config

import me.syari.sec_story.paper.library.config.content.ConfigContent
import me.syari.sec_story.paper.library.config.content.ConfigContentError
import me.syari.sec_story.paper.library.config.content.ConfigContents
import me.syari.sec_story.paper.library.config.content.ConfigItemStack
import me.syari.sec_story.paper.library.item.CustomItemStack
import me.syari.sec_story.paper.library.message.SendMessage.send
import me.syari.sec_story.paper.library.message.SendMessage.sendConsole
import org.bukkit.Bukkit.getWorld
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.*

class CustomConfig(val plugin: JavaPlugin, private val output: CommandSender, val file_name: String, private val dir: File, deleteIfEmpty: Boolean) {
    private var file = File(dir, file_name)
    private val config: YamlConfiguration
    private val path: String
    private val unused: MutableSet<String>

    init {
        config = YamlConfiguration.loadConfiguration(file)
        path = file.path.substringAfter(plugin.dataFolder.path).substring(1)
        if(! file.exists()) {
            try {
                file.createNewFile()
                sendConsole("[${plugin.name} | CustomConfig] &f$path の作成に成功しました")
            } catch(ex: IOException) {
                sendConsole("[${plugin.name} | CustomConfig] &c$path の作成に失敗しました")
            }
        } else if(file.length() == 0L && deleteIfEmpty) {
            sendConsole("[${plugin.name} | CustomConfig] &e$path は中身が存在しないので削除されます")
            delete()
        }
        unused = config.getConfigurationSection("")?.getKeys(true) ?: mutableSetOf()
    }

    inline fun with(run: CustomConfig.() -> Unit) {
        run.invoke(this)
    }

    fun checkUnused() {
        unused.forEach {
            unusedError(it)
        }
    }

    fun get(path: String): Any? {
        unused.remove(path)
        return config.get(path)
    }

    private inline fun <reified T> get(path: String, typeName: String, sendNotFound: Boolean): T? {
        return if(config.contains(path)) {
            val g = get(path)
            if(g is T) {
                g
            } else {
                typeMismatchError<T>(path, typeName)
            }
        } else {
            notFoundError(path, sendNotFound)
        }
    }

    private inline fun <reified T> getList(path: String, typeName: String, sendNotFound: Boolean): List<T>? {
        return get<List<*>>(path, "List", sendNotFound)?.let {
            val list = mutableListOf<T>()
            it.forEachIndexed { i, f ->
                if(f is T) {
                    list.add(f)
                } else {
                    typeMismatchInListError(path, i, typeName)
                }
            }
            list
        }
    }

    private fun getNumber(path: String, typeName: String, sendNotFound: Boolean): Number? {
        return get(path, typeName, sendNotFound)
    }

    fun getInt(path: String, sendNotFound: Boolean = true): Int? {
        return getNumber(path, "Int", sendNotFound)?.toInt()
    }

    fun getInt(path: String, def: Int, sendNotFound: Boolean = true): Int {
        return getInt(path, sendNotFound) ?: def
    }

    fun getDouble(path: String, sendNotFound: Boolean = true): Double? {
        return getNumber(path, "Double", sendNotFound)?.toDouble()
    }

    fun getDouble(path: String, def: Double, sendNotFound: Boolean = true): Double {
        return getDouble(path, sendNotFound) ?: def
    }

    private fun getLong(path: String, sendNotFound: Boolean = true): Long? {
        return getNumber(path, "Long", sendNotFound)?.toLong()
    }

    fun getLong(path: String, def: Long, sendNotFound: Boolean = true): Long {
        return getLong(path, sendNotFound) ?: def
    }

    fun getString(path: String, sendNotFound: Boolean = true): String? {
        return get(path, "String", sendNotFound)
    }

    fun getString(path: String, def: String, sendNotFound: Boolean = true): String {
        return getString(path, sendNotFound) ?: def
    }

    fun getStringList(path: String, sendNotFound: Boolean = true): List<String>? {
        return getList(path, "String", sendNotFound)
    }

    fun getStringList(path: String, def: Collection<String>, sendNotFound: Boolean = true): MutableList<String> {
        return (getStringList(path, sendNotFound) ?: def).toMutableList()
    }

    private fun getLocation(g: String): Location? {
        val s = g.split(",\\s*".toRegex())
        val size = s.size
        when(size) {
            4, 6 -> {
                val w = getWorld(s[0]) ?: return nullError<Location>(path, "World(${s[0]})")
                val x = s[1].toDoubleOrNull() ?: return typeMismatchError<Location>(path, "Double")
                val y = s[2].toDoubleOrNull() ?: return typeMismatchError<Location>(path, "Double")
                val z = s[3].toDoubleOrNull() ?: return typeMismatchError<Location>(path, "Double")
                if(size == 4) return Location(w, x, y, z)
                val yaw = s[4].toFloatOrNull() ?: return typeMismatchError<Location>(path, "Float")
                val pitch = s[5].toFloatOrNull() ?: return typeMismatchError<Location>(path, "Float")
                return Location(w, x, y, z, yaw, pitch)
            }
        }
        return formatMismatchError(path)
    }

    private fun <T> getFromString(path: String, typeName: String, sendNotFound: Boolean, run: (String) -> T): T? {
        val g = get<String>(path, typeName, sendNotFound) ?: return null
        return run.invoke(g)
    }

    private fun <T> getListFromStringList(path: String, typeName: String, sendNotFound: Boolean, run: (String) -> T?): List<T>? {
        return get<List<*>>(path, "List", sendNotFound)?.let {
            val list = mutableListOf<T>()
            it.forEachIndexed { i, f ->
                if(f is String) {
                    run.invoke(f)?.let { loc -> list.add(loc) }
                } else {
                    typeMismatchInListError(path, i, typeName)
                }
            }
            list
        }
    }

    fun getLocation(path: String, sendNotFound: Boolean = true): Location? {
        return getFromString(path, "String(Location)", sendNotFound){ getLocation(it) }
    }

    fun getLocationList(path: String, sendNotFound: Boolean = true): List<Location>? {
        return getListFromStringList(path, "String(Location)", sendNotFound){ getLocation(it) }
    }

    fun getLocationList(path: String, def: List<Location>, sendNotFound: Boolean = true): List<Location> {
        return getLocationList(path, sendNotFound) ?: def
    }

    private fun getBoolean(path: String, sendNotFound: Boolean = true): Boolean? {
        return get(path, "Boolean", sendNotFound)
    }

    fun getBoolean(path: String, def: Boolean, sendNotFound: Boolean = true): Boolean {
        return getBoolean(path, sendNotFound) ?: def
    }

    private fun getCustomItemStackListFromStringList(
        path: String, sendNotFound: Boolean = true, slip: Int = 0
    ): List<ConfigLoadItem>? {
        return getListFromStringList(path, "String(CustomItemStack)", sendNotFound){
            val s = it.split("\\s+".toRegex())
            when(s.size) {
                2 + slip, 3 + slip -> {
                    val label = s[0 + slip]
                    val id = s[1 + slip]
                    val item = ConfigItemStack.getItem(label, id, s.getOrNull(2 + slip))
                    if(item != null) {
                        ConfigLoadItem(item, it, label, id)
                    } else {
                        nullError<ConfigLoadItem>(path, "CustomItemStack(${s[0 + slip]} ${s[1 + slip]})")
                    }
                }
                else -> formatMismatchError(path)
            }
        }
    }

    fun getCustomItemStackListFromStringList(
        path: String, def: List<CustomItemStack>, sendNotFound: Boolean = true, slip: Int = 0
    ): MutableList<CustomItemStack> {
        return (getCustomItemStackListFromStringList(path, sendNotFound, slip)?.map { it.item } ?: def).toMutableList()
    }

    private fun getConfigLoadItem(g: String, slip: Int): ConfigLoadItem? {
        val s = g.split("\\s+".toRegex())
        return when(s.size) {
            2 + slip, 3 + slip -> {
                val label = s[0 + slip]
                val id = s[1 + slip]
                val item = ConfigItemStack.getItem(
                    label,
                    id,
                    s.getOrNull(2 + slip)
                ) ?: return nullError<ConfigLoadItem>(path, "CustomItemStack(${s[0 + slip]} ${s[1 + slip]})")
                ConfigLoadItem(item, g, label, id)
            }
            else -> formatMismatchError(path)
        }
    }

    fun getCustomItemStackFromString(path: String, sendNotFound: Boolean = true, slip: Int = 0): ConfigLoadItem? {
        return getFromString(path, "String(CustomItemStack)", sendNotFound){ g -> getConfigLoadItem(g, slip) }
    }

    fun getCustomItemStackFromString(
        path: String, def: CustomItemStack, sendNotFound: Boolean = true, slip: Int = 0
    ): CustomItemStack {
        return getCustomItemStackFromString(path, sendNotFound, slip)?.item ?: def
    }

    private fun getCustomItemStackList(path: String, sendNotFound: Boolean = true): List<CustomItemStack>? {
        return getList(path, "CustomItemStack", sendNotFound)
    }

    fun getCustomItemStackList(
        path: String, def: Collection<CustomItemStack>, sendNotFound: Boolean = true
    ): MutableList<CustomItemStack> {
        return (getCustomItemStackList(path, sendNotFound) ?: def).toMutableList()
    }

    fun getItemStack(path: String, sendNotFound: Boolean = true): ItemStack? {
        return get(path, "ItemStack", sendNotFound)
    }

    fun getDate(path: String, sendNotFound: Boolean = true): Date? {
        return get(path, "Date", sendNotFound)
    }

    private fun getColor(path: String, sendNotFound: Boolean = true): Color? {
        return get(path, "Color", sendNotFound)
    }

    fun getColor(path: String, def: Color, sendNotFound: Boolean = true): Color {
        return getColor(path, sendNotFound) ?: def
    }

    fun getConfigContentsFromList(path: String, sendNotFound: Boolean = true): ConfigContents {
        val list = ConfigContents()
        getStringList(path, sendNotFound)?.forEach { s ->
            val c = ConfigContent.getContent(s)
            if(c is ConfigContentError) {
                send(c.msg)
            } else {
                list.addContent(c)
            }
        }
        return list
    }

    fun contains(path: String) = config.contains(path)

    fun getSection(
        path: String, sendNotFound: Boolean = true
    ) = config.getConfigurationSection(path)?.getKeys(false) ?: notFoundError<Set<String>>(path, sendNotFound)

    fun set(path: String, value: Any?, save: Boolean = true) {
        config.set(path, value)
        if(save) save()
    }

    fun rename(newName: String): Boolean{
        val list = file.list() ?: return false
        if(list.contains(newName)) return false
        return try {
            file.renameTo(File(dir, newName))
            true
        } catch (ex: SecurityException){
            false
        } catch (ex: NullPointerException){
            false
        }
    }

    fun save() {
        config.save(file)
        if(file.length() == 0L) {
            delete()
        }
    }

    fun delete() {
        file.delete()
        sendConsole("[${plugin.name}|$path] &f削除に成功しました")
    }

    fun send(msg: String) {
        output.send("&b[${plugin.name}|$path] $msg")
    }

    private fun unusedError(path: String) {
        send("&d$path is not used")
    }

    private fun <T> notFoundError(path: String, sendNotFound: Boolean): T? {
        if(sendNotFound) send("&c$path is not found.")
        return null
    }

    fun unloadError(path: String, cause: String) {
        send("&c$path is unused. ($cause)")
    }

    fun nullError(path: String, thing: String) {
        send("&c$path $thing is null.")
    }

    private fun <T> nullError(path: String, thing: String): T? {
        nullError(path, thing)
        return null
    }

    private fun <T> formatMismatchError(path: String): T? {
        send("&c$path is incorrect format.")
        return null
    }

    fun typeMismatchError(path: String, type: String) {
        send("&c$path is not $type type.")
    }

    private fun <T> typeMismatchError(path: String, type: String): T? {
        typeMismatchError(path, type)
        return null
    }

    private fun typeMismatchInListError(path: String, index: Int, type: String) {
        send("&c$path:${index + 1} is not $type type.")
    }
}