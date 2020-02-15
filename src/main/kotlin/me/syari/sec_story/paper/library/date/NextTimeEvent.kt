package me.syari.sec_story.paper.library.date

import me.syari.sec_story.paper.library.event.CustomEvent
import java.time.DayOfWeek

open class NextTimeEvent(val dayOfWeek: DayOfWeek, val time: String): CustomEvent()