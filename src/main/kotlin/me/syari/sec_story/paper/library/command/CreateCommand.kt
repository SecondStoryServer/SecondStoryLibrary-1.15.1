package me.syari.sec_story.paper.library.command

import me.syari.sec_story.paper.library.Main.Companion.plugin
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender

object CreateCommand {
    fun elementIf(bool: Boolean, e: Collection<String>?, not: Collection<String>? = listOf()): CommandTabElement {
        return CommandTabElement(if(bool) e ?: listOf() else not ?: listOf())
    }

    fun elementIf(bool: Boolean, vararg e: String, not: Collection<String>? = listOf()): CommandTabElement {
        return elementIf(bool, e.toList(), not)
    }

    fun elementIfOp(
        sender: CommandSender, e: Collection<String>?, not: Collection<String>? = listOf()
    ): CommandTabElement {
        return elementIf(sender.isOp, e ?: listOf(), not)
    }

    fun elementIfOp(sender: CommandSender, vararg e: String, not: Collection<String>? = listOf()): CommandTabElement {
        return elementIfOp(sender, e.toList(), not)
    }

    fun element(e: Collection<String>?): CommandTabElement {
        return CommandTabElement(e ?: listOf())
    }

    fun element(vararg e: String): CommandTabElement {
        return element(e.toList())
    }

    fun element(run: () -> Collection<String>?): CommandTabElement {
        return CommandTabElement(run.invoke() ?: listOf())
    }

    fun tab(vararg arg: String, tab: (CommandSender, CommandArg) -> CommandTabElement?): CommandTab {
        return CommandTab(arg.toList(), tab)
    }

    fun createCmd(
        cmd: String, messagePrefix: String, vararg tab: CommandTab, prefix: String = "SecondStory", description: String = "", usage: String = "/", alias: List<String> = listOf(), execute: CommandMessage.(CommandSender, CommandArg) -> Unit
    ) {
        registerCommand(prefix, object: Command(cmd, description, usage, alias) {
            override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                val message = CommandMessage(messagePrefix, sender)
                execute.invoke(message, sender, CommandArg(args, message))
                return true
            }

            override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
                val message = CommandMessage(messagePrefix, sender)
                val ret = mutableListOf<String>()
                val j = args.joinToString(separator = " ").toLowerCase()
                val size = args.size - 1
                tab.forEach { t ->
                    val element = t.tab.invoke(sender, CommandArg(args, message))?.element ?: return@forEach
                    if(t.arg.isEmpty()) {
                        if(size == 0) ret.addAll(element.filter { it.toLowerCase().startsWith(j) })
                    } else {
                        t.arg.forEach { f ->
                            val l = f.split("\\s+".toRegex())
                            if(l.size == size) {
                                val a = if(f.contains('*')) {
                                    val s = StringBuilder()
                                    l.forEachIndexed { i, e ->
                                        if(e != "*") {
                                            s.append("$e ")
                                        } else {
                                            s.append("${args[i]} ")
                                        }
                                    }
                                    s.toString().substringBeforeLast(" ")
                                } else {
                                    f
                                }
                                ret.addAll(element.filter { "$a $it".toLowerCase().startsWith(j) })
                            }
                        }
                    }
                }
                ret.sort()
                return ret
            }
        })
    }

    private fun registerCommand(prefix: String, cmd: Command) {
        try {
            val commandMapField = plugin.server.javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            val commandMap = commandMapField.get(plugin.server) as CommandMap
            commandMap.register(prefix, cmd)
            commandMapField.isAccessible = false
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    val offlinePlayers: CommandTabElement
        get() {
            val ret = mutableListOf<String>()
            plugin.server.offlinePlayers.forEach { f -> f?.name?.let { ret.add(it) } }
            return CommandTabElement(ret)
        }

    val onlinePlayers: CommandTabElement
        get() {
            val ret = mutableListOf<String>()
            plugin.server.onlinePlayers.forEach { f -> if(f != null) ret.add(f.name) }
            return CommandTabElement(ret)
        }
}