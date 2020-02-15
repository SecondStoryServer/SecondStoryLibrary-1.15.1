package me.syari.sec_story.paper.library.particle

import me.syari.sec_story.paper.library.scheduler.CustomScheduler.runRepeatTimes
import me.syari.sec_story.paper.library.scheduler.CustomScheduler.runTimer
import me.syari.sec_story.paper.library.scheduler.CustomTask
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

interface MultipleParticle {
    val element: ParticleElement
    val list: List<Location>

    fun spawn(count: Int, speed: Double){
        list.forEach {
            element.spawn(it, count, speed, 0.0, 0.0, 0.0)
        }
    }

    fun spawnTimer(count: Int, speed: Double, plugin: JavaPlugin, period: Long, delay: Long = 0): CustomTask? {
        return runTimer(plugin, period, delay) {
            spawn(count, speed)
        }
    }

    fun spawnRepeatTimes(count: Int, speed: Double, plugin: JavaPlugin, period: Long, times: Int, delay: Long = 0): CustomTask? {
        return runRepeatTimes(plugin, period, times, delay) {
            spawn(count, speed)
        }
    }
}