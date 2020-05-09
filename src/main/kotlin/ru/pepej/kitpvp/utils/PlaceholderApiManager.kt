package ru.pepej.kitpvp.utils

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.utils.TimeUtil.formatTime

class PlaceholderApiManager : PlaceholderExpansion() {
    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun getAuthor(): String {
        return "pepej"
    }

    override fun getIdentifier(): String {
        return "kitpvp"
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(p: Player?, params: String): String? {
        p ?: return ""
        when (params) {
            "currentkit" -> return if (p.getKitByPlayer() != null) p.getKitByPlayer()!!.formattedName else +"&bУ вас нет кита!"
            "kills" -> return "${UserManager.getUser(p).getStat(StatType.KILLS)}"
            "deaths" -> return "${UserManager.getUser(p).getStat(StatType.DEATHS)}"
            "killstreak" -> return "${UserManager.getUser(p).getStat(StatType.KILLSTREAK)}"
            "kits" -> return "${UserManager.getUser(p).getStat(StatType.KITS_PICKED)}"
            "playtime" -> return if (timesPlayed.containsKey(p.uniqueId)) formatTime(timesPlayed[p.uniqueId]!!) else "0"
        }
        return null
    }
}