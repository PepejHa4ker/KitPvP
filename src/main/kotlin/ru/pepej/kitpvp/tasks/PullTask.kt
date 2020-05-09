package ru.pepej.kitpvp.tasks

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import ru.pepej.kitpvp.utils.message

class PullTask(private val p: Player, private val to: Location, private val speed: Double, private var time: Int) :
    BukkitRunnable() {
    override fun run() {
        time--
        val loc = p.location
        val x = loc.x - to.x
        val y = loc.y - to.y
        val z = loc.z - to.z
        val velocity = Vector(x, y, z).normalize().multiply(-speed)
        p.velocity = velocity

        when (time) {
            0 -> {
                this.cancel()
                p.message("&cCancelled")
                p.world.createExplosion(to, 10f)
            }
        }
    }
}