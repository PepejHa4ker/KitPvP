package ru.pepej.kitpvp.tasks

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class HookTask(private val p: Player, private val to: Location, private val speed: Double, private var timer: Int) : BukkitRunnable() {

    override fun run() {
        timer--
        val loc = p.location
        val x = loc.x - to.x
        val y = loc.y - to.y
        val z = loc.z - to.z
        val velocity = Vector(x, y, z).normalize().multiply(-speed);
        p.velocity = velocity

        when (timer) {
            0 -> {
                if(p.health > 6.0) {
                    p.health = p.health - 6
                } else {
                    p.damage(10000.0)
                }
                this.cancel()
            }
        }
    }

}