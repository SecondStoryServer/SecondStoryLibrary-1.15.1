package me.syari.sec_story.paper.library.particle

import me.syari.sec_story.paper.library.world.region.PolyRegion
import org.bukkit.Location

class ParticlesPolyPoints(override val element: ParticleElement, poly: PolyRegion): MultipleParticle {
    override val list: List<Location>

    init {
        val world = poly.world
        val yArray = arrayOf(poly.max.blockY + 0.5, poly.min.blockY + 0.5)
        val init = mutableListOf<Location>()
        poly.points.forEach {
            yArray.forEach { y ->
                init.add(Location(world, it.x + 0.5 , y, it.z + 0.5))
            }
        }
        list = init
    }
}