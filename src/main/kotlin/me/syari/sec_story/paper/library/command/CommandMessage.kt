package me.syari.sec_story.paper.library.command

import me.syari.sec_story.paper.library.message.SendMessage.send
import org.bukkit.command.CommandSender

class CommandMessage(private val prefix: String, private val sender: CommandSender) {
    fun CommandSender.sendWithPrefix(message: String){
        send("&b[$prefix] &r$message")
    }

    fun sendWithPrefix(message: String){
        sender.sendWithPrefix(message)
    }

    fun sendWithPrefix(builder: StringBuilder){
        sendWithPrefix(builder.toString())
    }

    fun errorNotEnterPlayer(){
        sendWithPrefix("&cプレイヤーを入力してください")
    }

    fun errorNotFoundPlayer(){
        sendWithPrefix("&cプレイヤーが見つかりませんでした")
    }

    fun errorNotEnterType(){
        sendWithPrefix("&cタイプを入力してください")
    }

    fun errorNotFoundType(){
        sendWithPrefix("&cタイプが見つかりませんでした")
    }

    fun errorNotEnterId(){
        sendWithPrefix("&cIDを入力してください")
    }

    fun errorAlreadyExistId(){
        sendWithPrefix("&c既に存在するIDです")
    }

    fun errorNotExistId(){
        sendWithPrefix("&c存在しないIDです")
    }

    fun errorNotEnterNewId(){
        sendWithPrefix("&c新しいIDを入力してください")
    }

    fun errorNotEnterName(){
        sendWithPrefix("&c名前を入力してください")
    }

    fun errorNotEnterPage(){
        sendWithPrefix("&cページ番号を入力してください")
    }

    fun errorOnlyPlayer(){
        sendWithPrefix("&cコンソールから実行できないコマンドです")
    }

    fun errorNotSelectPoly(){
        sendWithPrefix("&c多辺形選択してください &7//sel poly")
    }

    fun errorNotSelectTwo(){
        sendWithPrefix("&c二箇所選択してください &7//sel cuboid")
    }

    fun sendHelp(vararg cmd_desc: Pair<String, String>): SendHelpIfOp{
        sendList("コマンド一覧", cmd_desc.map { "/${it.first} &7${it.second}" })
        return SendHelpIfOp(this)
    }

    class SendHelpIfOp(val message: CommandMessage) {
        fun ifOp(vararg cmd_desc: Pair<String, String>){
            if(message.sender.isOp){
                message.sendList("", cmd_desc.map { "/${it.first} &7${it.second}" })
            }
        }
    }

    fun sendList(title: String = "", collection: Collection<String>){
        if(title.isNotEmpty()) sendWithPrefix("&f$title")
        val builder = StringBuilder()
        collection.forEach {
            builder.appendln("&7- &a$it")
        }
        sender.send(builder)
    }
}