@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.kitData
import ru.pepej.kitpvp.KitPvPCore.Companion.playerData
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerRemoveKitEvent
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.kit.KitManager.kitDelay
import ru.pepej.kitpvp.utils.*
import java.util.*

class RemoveCommand
    : SubCommand(
    name = "remove",
    description = "Удалить кит",
    syntax = "/kits remove <Кит>",
    alias = "r",
    tabCompletable = true
) {
    override fun execute(player: Player, args: Array<out String>) {

        if (args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val name = args[0]
        if (getKitByName(name) == null) {
            player.message(KIT_NOT_EXIST)
            return
        }
        val k = getKitByName(name)!!
        val e = PlayerRemoveKitEvent(player, k)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return
        }
         playerData.getConfigurationSection("").getKeys(true).forEach {
            if(playerData.get("$it.lastclass") == name) {
                playerData.set("$it.lastclass", null)
                currentKit.remove(UUID.fromString(it))
                playerData.save()
            }
        }

        for (s in kitData.getConfigurationSection("").getKeys(false)) {
            if (s == name) {
                val kit = getKitByName(name)!!
                kitDelay.remove(kit)
                kitData.set(s, null)
                kitData.save()
            }
        }
        val keys = kitConfig.getConfigurationSection(name).getKeys(true)
        keys.forEach {
            kitConfig.set("$name.$it", null)
        }
        kitConfig.set(name, null)
        kitConfig.save()
        kit.remove(getKitByName(name)!!)
        player.message("&cКит &6$name &cуспешно удалён")

    }
}