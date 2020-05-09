package ru.pepej.kitpvp.tasks

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import ru.pepej.kitpvp.listeners.Listener.Companion.playersToBlaze

class BlazeRodTask(private val p: Player) : BukkitRunnable() {

    private var timer = 0

    override fun run() {
        timer++
        if (timer % 5 == 0) {
           for(pl in playersToBlaze[p]!!) {
               pl.playSound(pl.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1f)
           }

        }
        if (timer == 15) {
            for(pl in playersToBlaze[p]!!) {
                playersToBlaze.remove(pl)
            }
            this.cancel()

        }


    }
}