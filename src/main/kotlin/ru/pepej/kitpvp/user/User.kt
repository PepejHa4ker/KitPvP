package ru.pepej.kitpvp.user

import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerChangeStatisticEvent
import ru.pepej.kitpvp.user.UserManager.saveStatistic
import java.util.*


data class User(val player: Player) {
    private val stats = EnumMap<StatType, Int>(StatType::class.java)

    fun getStat(stat: StatType): Int? {
        if (!stats.containsKey(stat)) {
            stats[stat] = 0
            return 0
        } else if (stats[stat] == null) {
            return 0
        }
        return stats[stat]
    }

    fun setStat(stat: StatType, i: Int) {
        stats[stat] = i
        saveStatistic(this, stat)
        plugin.server.pluginManager.callEvent(PlayerChangeStatisticEvent(player,  stat, i))
    }

    fun addStat(stat: StatType, i: Int) {
        stats[stat] = getStat(stat)!! + i
        saveStatistic(this, stat)
        plugin.server.pluginManager.callEvent(PlayerChangeStatisticEvent(player, stat, i))
    }

}



