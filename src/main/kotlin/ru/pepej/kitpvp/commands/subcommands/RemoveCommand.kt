@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
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
    type = CommandType.KITS
) {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {


        if(sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        if (args.size < 2) {
            sender.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
        val k = getKitByName(kitName) ?: return player.message(KIT_NOT_EXIST)
        val e = PlayerRemoveKitEvent(player, k)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return
        }
         playerData.getConfigurationSection("").getKeys(true).forEach {
            if(playerData.get("$it.lastclass") == kitName) {
                playerData.set("$it.lastclass", null)
                currentKit.remove(UUID.fromString(it))
                playerData.save()
            }
        }

        for (s in kitData.getConfigurationSection("").getKeys(false)) {
            if (s == kitName) {
                val kit = getKitByName(kitName)!!
                kitDelay.remove(kit)
                kitData.set(s, null)
                kitData.save()
            }
        }
        val keys = kitConfig.getConfigurationSection(kitName).getKeys(true)
        keys.forEach {
            kitConfig.set("$kitName.$it", null)
        }
        kitConfig.set(kitName, null)
        kitConfig.save()
        kit.remove(getKitByName(kitName)!!)
        player.message("&cКит &6$kitName&c успешно удалён")

    }
}