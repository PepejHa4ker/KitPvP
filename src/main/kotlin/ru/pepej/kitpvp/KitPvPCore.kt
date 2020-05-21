@file:Suppress("UNCHECKED_CAST")

package ru.pepej.kitpvp

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import ru.pepej.kitpvp.api.events.server.ServerUpdateEvent
import ru.pepej.kitpvp.api.events.server.UpdateType
import ru.pepej.kitpvp.commands.CommandManager
import ru.pepej.kitpvp.configuration.Config
import ru.pepej.kitpvp.listeners.EventListener
import ru.pepej.kitpvp.listeners.KitListener
import ru.pepej.kitpvp.menu.PlayerMenuUtility
import ru.pepej.kitpvp.model.Kit
import ru.pepej.kitpvp.model.KitManager.getKitByName
import ru.pepej.kitpvp.model.KitManager.kitDelay
import ru.pepej.kitpvp.model.KitManager.setupKits
import ru.pepej.kitpvp.utils.PlaceholderApiManager
import ru.pepej.kitpvp.utils.message
import java.util.*
import kotlin.collections.HashMap


class KitPvPCore : KotlinPlugin() {
    companion object {
        private val playerMenuUtilitymap = HashMap<Player, PlayerMenuUtility>()
        val timesPlayed = HashMap<UUID, Int>()
        val currentKit = HashMap<UUID, Kit>()
        lateinit var kitConfig: Config
        lateinit var playerData: Config
        lateinit var kitData: Config
        val kit = HashSet<Kit>()
        lateinit var economy: Economy
        lateinit var cs: ConsoleCommandSender
        lateinit var plugin: KitPvPCore
            private set
        fun getPlayerMenuUtility(p: Player): PlayerMenuUtility {
            return if (playerMenuUtilitymap.containsKey(p)) {
                playerMenuUtilitymap[p]!!
            } else {
                val playerMenuUtility = PlayerMenuUtility(p)
                playerMenuUtilitymap[p] = playerMenuUtility
                playerMenuUtility
            }
        }
    }

    init {
        plugin = this
        cs = Bukkit.getConsoleSender()
    }
    override fun onPluginEnable() {
        kitConfig = Config("kits")
        kitData = Config("kitsdata")
        playerData = Config("playerData")
        for(type in UpdateType.values()) {
            server.scheduler.scheduleSyncRepeatingTask(plugin, {
                server.pluginManager.callEvent(ServerUpdateEvent(type)) // Registering new update-threads
            }, 0, type.delay)
        }
        EventListener()
        KitListener(this)
        PlaceholderApiManager().register()
        setupKits()
        for (p in server.onlinePlayers) {
            timesPlayed[p.uniqueId] = playerData.getInt("${p.uniqueId}.timesplayed")
            if (playerData.getString("${p.uniqueId}.lastclass") != null) {
                val kit = getKitByName(playerData.getString("${p.uniqueId}.lastclass"))
                currentKit[p.uniqueId] = kit!!
            }
        }
        getCommand("kits").executor = CommandManager()
        if (!setupEconomy()) {
            server.pluginManager.disablePlugin(this)
            return
        }
        cs.message("&4&m---------")
        cs.message(" &3KitPvP")
        cs.message(" &4Enabled!")
        cs.message(" &3Author: &apepej")
        cs.message("&4&m---------")
    }
    // Setting up vault
    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        economy = rsp.provider
        return true
    }


    override fun onPluginDisable() {
        kitDelay.keys.forEach {
            if(kitDelay[it]!! > 0) {
                kitData.set("${it.kitName}.delay", kitDelay[it]!!)
            } else {
                kitData.set("${it.kitName}.delay", 0)
            }
            kitData.save()
        }

        for (p in server.onlinePlayers) {
            p.closeInventory()
            if (timesPlayed.contains(p.uniqueId)) {
                playerData.set("${p.uniqueId}.timesplayed", timesPlayed[p.uniqueId])
                playerData.save()
            }
            if (currentKit.contains(p.uniqueId)) {
                playerData.set("${p.uniqueId}.lastclass", currentKit[p.uniqueId]!!.kitName)
                playerData.save()
            }
        }
    }

}