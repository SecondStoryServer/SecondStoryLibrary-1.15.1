package me.syari.sec_story.paper.library.particle

import org.bukkit.Location
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleCircle(override val element: ParticleElement, center: Location, radius: Double, amount: Int, addX: Double = 0.0, addY: Double = 0.0, addZ: Double = 0.0): MultipleParticle{
    override val list: List<Location>

    init {
        val world = center.world
        val increment: Double = 2 * PI / amount
        val circle = mutableListOf<Location>()
        val vector = center.clone().add(addX, addY, addZ)
        for (i in 0 until amount) {
            val angle = i * increment
            val x = vector.x + radius * cos(angle)
            val z = vector.z + radius * sin(angle)
            circle.add(Location(world, x, vector.y, z))
        }
        this.list = circle
    }
}