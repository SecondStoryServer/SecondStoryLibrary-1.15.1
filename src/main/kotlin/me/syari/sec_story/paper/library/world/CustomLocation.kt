package me.syari.sec_story.paper.library.world

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.NumberConversions

class CustomLocation(
    var world: World, var x: Double, var y: Double, var z: Double, var yaw: Float = 0.0F, var pitch: Float = 0.0F
) {
    companion object {
        fun fromNullable(location: Location?): CustomLocation? {
            return location?.let { CustomLocation(it) }
        }
    }

    constructor(location: Location): this(
        location.world, location.x, location.y, location.z, location.yaw, location.pitch
    )

    val blockX by lazy { NumberConversions.floor(x) }
    val blockY by lazy { NumberConversions.floor(y) }
    val blockZ by lazy { NumberConversions.floor(z) }

    override fun toString() = "${world.name}, $x, $y, $z"

    val toStringWithYawPitch by lazy { "${world.name}, $x, $y, $z, $yaw, $pitch" }

    val toLocation by lazy { Location(world, x, y, z, yaw, pitch) }

    val asBlockTriple by lazy { Triple(blockX, blockY, blockZ) }
}