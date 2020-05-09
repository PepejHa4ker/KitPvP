package ru.pepej.kitpvp.tasks

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TorpedoTask(private val p: Player) : BukkitRunnable() {

    private var timer = 0
    override fun run() {
        timer++
        when (timer) {
            3 -> {
                p.world.createExplosion(p.location, 0.1f)
                p.health = p.health - 3
            }
            5 -> {
                this.cancel()
            }
        }


    }

}