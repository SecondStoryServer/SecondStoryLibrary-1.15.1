package me.syari.sec_story.paper.library.world

import org.bukkit.Location
import org.bukkit.util.NumberConversions

data class Vector3D(val x: Double, val y: Double, val z: Double){
    constructor(x: Int, y: Int, z: Int): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(location: Location): this(location.x, location.y, location.z)

    val blockX by lazy { NumberConversions.floor(x) }
    val blockY by lazy { NumberConversions.floor(y) }
    val blockZ by lazy { NumberConversions.floor(z) }

    val asBlockTriple by lazy { Triple(blockX, blockY, blockZ) }
}