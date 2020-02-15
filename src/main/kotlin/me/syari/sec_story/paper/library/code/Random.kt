package me.syari.sec_story.paper.library.code

object Random {
    fun randomPercent(percent: Int): Boolean{
        return (0 until 100).random() < percent
    }
}