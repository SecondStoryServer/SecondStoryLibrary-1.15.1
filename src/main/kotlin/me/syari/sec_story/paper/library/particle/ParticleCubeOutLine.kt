package me.syari.sec_story.paper.library.particle

import me.syari.sec_story.paper.library.world.region.CuboidRegion
import org.bukkit.Location
import java.util.*

class ParticleCubeOutLine(override val element: ParticleElement, cube: CuboidRegion): MultipleParticle {
    override val list: List<Location>

    init {
        val init: MutableList<Location> = ArrayList()
        val world = cube.world
        val (maxX, maxY, maxZ) = cube.max.asBlockTriple
        val (minX, minY, minZ) = cube.min.asBlockTriple

        fun add(x: Int, y: Int, z: Int){
            init.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
        }

        for (x in minX..maxX) {
            add(x, minY, minZ)
            add(x, maxY, minZ)
            add(x, minY, maxZ)
            add(x, maxY, maxZ)
        }
        for (y in minY..maxY) {
            add(minX, y, minZ)
            add(maxX, y, minZ)
            add(minX, y, maxZ)
            add(maxX, y, maxZ)
        }
        for (z in minZ..maxZ) {
            add(minX, minY, z)
            add(maxX, minY, z)
            add(minX, maxY, z)
            add(maxX, maxY, z)
        }
        init.forEach {
            it.add(0.5, 0.5, 0.5)
        }
        list = init
    }
}