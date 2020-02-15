package me.syari.sec_story.paper.library.world.region

import me.syari.sec_story.paper.library.world.CustomLocation
import org.bukkit.Location

interface Region {
    fun inRegion(loc: CustomLocation): Boolean
    fun inRegion(loc: Location) = inRegion(CustomLocation(loc))
}