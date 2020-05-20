package ru.pepej.kitpvp.listeners

import org.bukkit.Bukkit
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.api.events.server.ServerUpdateEvent
import ru.pepej.kitpvp.api.events.server.UpdateType
import ru.pepej.kitpvp.utils.KListener
import ru.pepej.kitpvp.utils.event

class KitListener(override val plugin: KitPvPCore) : KListener<KitPvPCore> {

    init {
        event<ServerUpdateEvent> {
            if (type != UpdateType.SECOND) return@event
            for (player in Bukkit.getOnlinePlayers()) {
                timesPlayed[player.uniqueId]!!.plus(1)

            }
        }
    }
}