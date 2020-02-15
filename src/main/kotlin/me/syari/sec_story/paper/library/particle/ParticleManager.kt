package me.syari.sec_story.paper.library.particle

import me.syari.sec_story.paper.library.world.region.CuboidRegion
import me.syari.sec_story.paper.library.world.region.PolyRegion
import org.bukkit.Location

object ParticleManager {
    fun circle(element: ParticleElement, center: Location, radius: Double, amount: Int, addX: Double = 0.0, addY: Double = 0.0, addZ: Double = 0.0): ParticleCircle {
        return ParticleCircle(element, center, radius, amount, addX, addY, addZ)
    }

    fun polyPoints(element: ParticleElement, region: PolyRegion): ParticlesPolyPoints{
        return ParticlesPolyPoints(element, region)
    }

    fun cubeOutLine(element: ParticleElement, region: CuboidRegion): ParticleCubeOutLine{
        return ParticleCubeOutLine(element, region)
    }
}