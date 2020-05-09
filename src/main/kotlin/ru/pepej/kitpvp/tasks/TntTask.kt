package ru.pepej.kitpvp.tasks


import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import ru.pepej.kitpvp.listeners.Listener.Companion.playersToBoom
import ru.pepej.kitpvp.utils.message

class TntTask(private val owner: Player) : BukkitRunnable() {
    private var timer = 0
    override fun run() {
        timer++
        if(playersToBoom[owner].isNullOrEmpty()) this.cancel()
        for(p in playersToBoom[owner]!!) {
            when (timer) {
                in 0..14 -> {
                    p.message("&cВзрыв через ${15 - timer}")
                }
                15 -> {
                    p.world.createExplosion(p.location, 0.1f, false)
                    if(p.health > 8.0) {
                        p.health = p.health - 8
                    } else {
                        p.damage(10000.0)
                    }
                    p.message("&c&lОх уж эти террористы.. Забрали 4 сердца..")
                    playersToBoom.remove(p)
                    this.cancel()
                }
            }
        }
    }
}