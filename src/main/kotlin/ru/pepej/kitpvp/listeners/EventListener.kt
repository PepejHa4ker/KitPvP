@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.listeners
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.KitPvPCore.Companion.playerData
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.api.events.player.PlayerKilledByPlayerEvent
import ru.pepej.kitpvp.menu.Menu
import ru.pepej.kitpvp.model.KitManager.getKitByName
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.user.UserManager.loadStatistics
import ru.pepej.kitpvp.utils.registerEvents

class EventListener : Listener {

    init {registerEvents(plugin)}

    @EventHandler
    fun onJoin(e: PlayerLoginEvent) {
        loadStatistics(UserManager.getUser(e.player))
        if (playerData.getString("${e.player.uniqueId}.timesplayed").isNullOrEmpty()) {
            playerData.set("${e.player.uniqueId}.timesplayed", 0)
            playerData.save()
        } else timesPlayed[e.player.uniqueId] = playerData.getInt("${e.player.uniqueId}.timesplayed")
        if (playerData.getString("${e.player.uniqueId}.lastclass") != null) {
            val kit = getKitByName(playerData.getString("${e.player.uniqueId}.lastclass"))!!
            currentKit[e.player.uniqueId] = kit
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        if (timesPlayed.contains(e.player.uniqueId)) {
            playerData.set("${e.player.uniqueId}.timesplayed", timesPlayed[e.player.uniqueId])
            playerData.save()
            timesPlayed.remove(e.player.uniqueId)
        }
        if (currentKit.contains(e.player.uniqueId)) {
            playerData.set("${e.player.uniqueId}.lastclass", currentKit[e.player.uniqueId]!!.kitName)
            playerData.save()
            currentKit.remove(e.player.uniqueId)
        }
    }

    @EventHandler
    fun onMenuClick(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        val holder = e.inventory.holder
        if(holder is Menu) {
            e.isCancelled = true
            if(e.currentItem == null) {
                return
            }
            holder.handleMenu(e)
        }
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        if (currentKit.contains(e.entity.uniqueId)) {
            if (e.entity.killer != null) {
                val damager = e.entity.killer
                val u = UserManager.getUser(damager)
                u.addStat(StatType.KILLS, 1)
                u.addStat(StatType.KILLSTREAK, 1)
                val event = PlayerKilledByPlayerEvent(e.entity, currentKit[e.entity.uniqueId]!!, damager)
                plugin.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return
                }
            }
            val u2 = UserManager.getUser(e.entity)
            u2.addStat(StatType.DEATHS, 1)
            u2.setStat(StatType.KILLSTREAK, 0)

        }
        currentKit.remove(e.entity.uniqueId)
    }
}
