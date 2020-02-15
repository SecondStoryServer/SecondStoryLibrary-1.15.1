package me.syari.sec_story.paper.library.config

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object CreateConfig {
    fun getConfigFile(plugin: JavaPlugin, output: CommandSender, file_name: String, deleteIfEmpty: Boolean = true): CustomConfig {
        var dir = plugin.dataFolder
        if(! dir.exists()) dir.mkdir()
        file_name.split("/".toRegex()).forEach { f ->
            if(f.endsWith(".yml")) {
                return CustomConfig(plugin, output, f, dir, deleteIfEmpty)
            } else {
                dir = File(dir, f)
                if(! dir.exists()) {
                    dir.mkdir()
                }
            }
        }
        throw Exception("File name does not end with '.yml'.")
    }

    fun getConfigDir(plugin: JavaPlugin, output: CommandSender, dir_name: String, deleteIfEmpty: Boolean = true): Map<String, CustomConfig> {
        var dir = plugin.dataFolder
        if(! dir.exists()) dir.mkdir()
        dir_name.split("/".toRegex()).forEach { f ->
            dir = File(dir, f)
            if(! dir.exists()) dir.mkdir()
        }
        val ret = mutableMapOf<String, CustomConfig>()
        dir.list()?.forEach { f ->
            if(f.endsWith(".yml")) {
                ret[f] = CustomConfig(plugin, output, f, dir, deleteIfEmpty)
            }
        }
        return ret
    }

    fun config(
        plugin: JavaPlugin, output: CommandSender, file_name: String, deleteIfEmpty: Boolean = true, command: CustomConfig.() -> Unit
    ): CustomConfig {
        val config = getConfigFile(plugin, output, file_name, deleteIfEmpty)
        config.command()
        return config
    }

    fun configDir(
        plugin: JavaPlugin, output: CommandSender, dir_name: String, deleteIfEmpty: Boolean = true, command: CustomConfig.() -> Unit
    ) {
        getConfigDir(plugin, output, dir_name, deleteIfEmpty).values.forEach { f ->
            f.command()
        }
    }

    fun containsFile(plugin: JavaPlugin, file_name: String): Boolean {
        var dir = plugin.dataFolder
        if(! dir.exists()) dir.mkdir()
        file_name.split("/".toRegex()).forEach { f ->
            if(f.endsWith(".yml")) {
                dir.list()?.forEach { l ->
                    if(f == l) {
                        return true
                    }
                }
            } else {
                dir = File(dir, f)
                if(! dir.exists()) {
                    return false
                }
            }
        }
        return false
    }
}