package ru.pepej.kitpvp.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
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
        val u = UserManager.getUser(p)
        when (params) {
            "currentkit" -> return if (p.getKitByPlayer() != null) p.getKitByPlayer()!!.formattedName else "У вас нет кита!"
            "kills"      -> return "${u.getStat(StatType.KILLS)}"
            "deaths"     -> return "${u.getStat(StatType.DEATHS)}"
            "killstreak" -> return "${u.getStat(StatType.KILLSTREAK)}"
            "kits"       -> return "${u.getStat(StatType.KITS_PICKED)}"
            "playtime"   -> return if (timesPlayed.containsKey(p.uniqueId)) timesPlayed[p.uniqueId]!!.toDateFormat() else "0"
        }
        return null
    }
}