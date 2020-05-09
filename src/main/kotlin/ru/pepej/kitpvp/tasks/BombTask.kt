package ru.pepej.kitpvp.tasks

import br.com.devsrsouza.kotlinbukkitapi.extensions.location.getNearbyEntities
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import ru.pepej.kitpvp.listeners.EventListener.Companion.isForceActivated
import ru.pepej.kitpvp.listeners.EventListener.Companion.picked
import ru.pepej.kitpvp.utils.message

class BombTask(private var timer: Int, private val owner: Player) : BukkitRunnable() {
    override fun run() {
        timer--
        if(isForceActivated.containsKey(owner) && isForceActivated[owner]!!) {
            picked[owner]!!.location.getNearbyEntities(5.0, 5.0, 5.0).filterIsInstance<Player>().forEach {
                it.message("&b&lНога...")
                it.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 60, 0))
            }
            picked[owner]!!.world.createExplosion(picked[owner]!!.location, 3f)
            picked[owner]!!.delete()
            picked.remove(owner)
            this.cancel()
        }
        when (timer) {
            0 -> {
                picked[owner]!!.location.getNearbyEntities(5.0, 5.0, 5.0).filterIsInstance<Player>().forEach {
                    it.message("&b&lНога...")
                    it.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 60, 0))

                }
                picked[owner]!!.world.createExplosion(picked[owner]!!.location, 3f)
                picked[owner]!!.delete()
                picked.remove(owner)
                this.cancel()
            }
        }
    }

}