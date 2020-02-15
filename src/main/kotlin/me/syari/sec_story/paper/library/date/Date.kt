package me.syari.sec_story.paper.library.date

import me.syari.sec_story.paper.library.Main.Companion.plugin
import me.syari.sec_story.paper.library.init.EventInit
import me.syari.sec_story.paper.library.scheduler.CustomScheduler.schedule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Date: EventInit {
    lateinit var today: LocalDate

    private var now = "null"
    private var bef = "null"
    private var day = DayOfWeek.MONDAY

    private fun loadDay() {
        val now = LocalDate.now()
        today = now
        day = now.dayOfWeek
    }

    private val timer = schedule(plugin) {
        now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        if(bef != now) {
            if(now == "00:00") {
                loadDay()
                NextDayEvent(day).callEvent()
            } else {
                NextTimeEvent(day, now).callEvent()
            }
            bef = now
        }
    }

    fun onEnable() {
        loadDay()
        timer.runTimer(20)
    }

    fun onDisable() {
        timer.cancel()
    }
}