package me.syari.sec_story.paper.library.world.region

import me.syari.sec_story.paper.library.world.CustomLocation
import me.syari.sec_story.paper.library.world.Vector2D
import me.syari.sec_story.paper.library.world.Vector3D
import org.bukkit.World

class PolyRegion(val world: World, val points: List<Vector2D>, val min: Vector3D, val max: Vector3D): Region {
    override fun inRegion(loc: CustomLocation): Boolean {
        val size = points.size
        return if (size < 3) {
            false
        } else {
            val (tX, tY, tZ) = loc.asBlockTriple
            if (world != loc.world || tX !in min.blockX .. max.blockX || tY !in min.blockY .. max.blockY || tZ !in min.blockZ .. max.blockZ) {
                false
            } else {
                var inside = false
                var (xOld, zOld) = points[size - 1].let { it.blockX to it.blockZ }
                for (i in 0 until size) {
                    val (xNew, zNew) = points[i].let { it.blockX to it.blockZ }
                    if (xNew == tX && zNew == tZ) {
                        return true
                    }
                    val x1: Int
                    val z1: Int
                    val x2: Int
                    val z2: Int
                    if (xNew > xOld) {
                        x1 = xOld
                        x2 = xNew
                        z1 = zOld
                        z2 = zNew
                    } else {
                        x1 = xNew
                        x2 = xOld
                        z1 = zNew
                        z2 = zOld
                    }
                    if (tX in x1..x2) {
                        val c = (tZ.toLong() - z1.toLong()) * (x2 - x1).toLong() - (z2.toLong() - z1.toLong()) * (tX - x1).toLong()
                        if (c == 0L) {
                            if (z1 <= tZ == tZ <= z2) {
                                return true
                            }
                        } else if (c < 0L && x1 != tX) {
                            inside = !inside
                        }
                    }
                    xOld = xNew
                    zOld = zNew
                }
                inside
            }
        }
    }
}