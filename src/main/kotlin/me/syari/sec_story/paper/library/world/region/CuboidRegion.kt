package me.syari.sec_story.paper.library.world.region

import me.syari.sec_story.paper.library.world.CustomLocation
import me.syari.sec_story.paper.library.world.Vector3D
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

class CuboidRegion(val world: World, pos1: Vector3D, pos2: Vector3D): Region {
    companion object {
        fun fromNullable(pos1: Location?, pos2: Location?): CuboidRegion? {
            return if(pos1 != null && pos2 != null && pos1.world == pos2.world) CuboidRegion(
                pos1.world, Vector3D(pos1), Vector3D(pos2)
            ) else null
        }
    }

    val max: Vector3D
    val min: Vector3D

    init {
        val parse = Triple(
            if (pos2.x < pos1.x) pos1.x to pos2.x else pos2.x to pos1.x,
            if (pos2.y < pos1.y) pos1.y to pos2.y else pos2.y to pos1.y,
            if (pos2.z < pos1.z) pos1.z to pos2.z else pos2.z to pos1.z
        )
        max = Vector3D(
            parse.first.first, parse.second.first, parse.third.first
        )
        min = Vector3D(
            parse.first.second, parse.second.second, parse.third.second
        )
    }

    val length by lazy { Triple(max.x - min.x, max.y - min.y, max.z - min.z) }

    override fun inRegion(loc: CustomLocation): Boolean {
        return loc.world == world && loc.x in min.x..max.x && loc.y in min.y..max.y && loc.z in min.z..max.z
    }

    private val asIntRange by lazy {
        Triple(min.x.toInt()..max.x.toInt(), min.y.toInt()..max.y.toInt(), min.z.toInt()..max.z.toInt())
    }

    fun isOverlapWith(other: CuboidRegion): Boolean {
        fun isOverlap(a: IntRange, b: IntRange): Boolean{
            return a.first in b || b.first in a
        }
        val r1 = asIntRange
        val r2 = other.asIntRange
        return isOverlap(r1.first, r2.first) && isOverlap(r1.second, r2.second) && isOverlap(r1.third, r2.third)
    }
}