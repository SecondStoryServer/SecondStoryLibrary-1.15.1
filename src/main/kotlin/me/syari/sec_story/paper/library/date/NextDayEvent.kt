package me.syari.sec_story.paper.library.date

import java.time.DayOfWeek

class NextDayEvent(dayOfWeek: DayOfWeek): NextTimeEvent(dayOfWeek, "00:00")