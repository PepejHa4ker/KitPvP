package ru.pepej.kitpvp.tasks

import org.bukkit.scheduler.BukkitRunnable
import ru.pepej.kitpvp.BlidnessArena
import ru.pepej.kitpvp.listeners.Listener.Companion.arenas
import ru.pepej.kitpvp.utils.message

class BlidnessArenaTask(private val a: BlidnessArena) : BukkitRunnable() {
    private var timer = 0
    override fun run() {
        timer++
        for(p in a.world.players) {
            when (timer) {
                 20 -> {
                    p.message("&6&lЗона будет убрана через ${25 - timer} секунд")
                }
                1 -> {
                    p.message("&6&lСейчас появится арена радиусом 15 блоков, войдя в которую вы получите ослепление на минуту.")
                    p.message("&6&lДлительность - 25 секунд.")
                }
                25 -> {
                    p.message("&6&lЗона убрана.")
                    arenas.remove(a)
                    cancel()
                }
            }
        }
    }
}