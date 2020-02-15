package me.syari.sec_story.paper.library.particle

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

sealed class ParticleElement(private val type: Particle){
    open fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double){
        location.world.spawnParticle(type, location, count, speed, offsetX, offsetY, offsetZ)
    }

    class Normal(type: Particle): ParticleElement(type)

    class ItemCrack(private val material: Material): ParticleElement(Particle.ITEM_CRACK){
        override fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double) {
            location.world.spawnParticle(Particle.ITEM_CRACK, location, count, speed, offsetX, offsetY, offsetZ, ItemStack(material))
        }
    }

    class BlockCrack(private val material: Material): ParticleElement(Particle.BLOCK_CRACK){
        override fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double) {
            location.world.spawnParticle(Particle.BLOCK_CRACK, location, count, speed, offsetX, offsetY, offsetZ, material.createBlockData())
        }
    }

    class BlockDust(private val material: Material): ParticleElement(Particle.BLOCK_DUST){
        override fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double) {
            location.world.spawnParticle(Particle.BLOCK_DUST, location, count, speed, offsetX, offsetY, offsetZ, material.createBlockData())
        }
    }

    class FallingDust(private val material: Material): ParticleElement(Particle.FALLING_DUST){
        override fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double) {
            location.world.spawnParticle(Particle.FALLING_DUST, location, count, speed, offsetX, offsetY, offsetZ, material.createBlockData())
        }
    }

    class RedStone(red: Int, blue: Int, green: Int, size: Float): ParticleElement(Particle.REDSTONE){
        private fun convertColor(value: Int): Int {
            return when {
                value < 0 -> 0
                255 < value -> 255
                else -> value
            }
        }

        private val dustOptions = Particle.DustOptions(Color.fromBGR(convertColor(blue), convertColor(green), convertColor(red)), size)

        override fun spawn(location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, speed: Double) {
            location.world.spawnParticle(Particle.REDSTONE, location, count, speed, offsetX, offsetY, offsetZ, dustOptions)
        }
    }
}