package me.syari.sec_story.paper.library.world

import org.bukkit.util.NumberConversions

data class Vector2D(val x: Double, val z: Double){
    constructor(x: Int, z: Int): this(x.toDouble(), z.toDouble())

    val blockX by lazy { NumberConversions.floor(x) }
    val blockZ by lazy { NumberConversions.floor(z) }
}