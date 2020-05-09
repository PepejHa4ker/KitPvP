package ru.pepej.kitpvp.user.data

import ru.pepej.kitpvp.KitPvPCore.Companion.playerData
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.User

object FileStats : UserData {

    override fun saveStatistic(user: User, stat: StatType) {
        playerData.set("${user.player.uniqueId}.${stat.displayName}", user.getStat(stat)!!)
        playerData.save()
    }

    override fun loadStatistics(user: User) {
        for(stat in StatType.values()) {
            user.setStat(stat, playerData.getInt("${user.player.uniqueId}.${stat.displayName}", 0))
        }
    }

}