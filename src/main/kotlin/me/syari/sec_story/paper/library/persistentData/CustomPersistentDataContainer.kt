package me.syari.sec_story.paper.library.persistentData

import org.bukkit.plugin.java.JavaPlugin

interface CustomPersistentDataContainer {
    fun <E> editPersistentData(plugin: JavaPlugin, run: CustomPersistentData.() -> E): E?

    fun <E> getPersistentData(plugin: JavaPlugin, run: CustomPersistentData.() -> E): E?
}
