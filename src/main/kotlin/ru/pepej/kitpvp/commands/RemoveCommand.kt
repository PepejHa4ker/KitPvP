@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
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

class RemoveCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            sender.message(NOT_ENOUGH_ARGS)
            return true
        }

        if(!sender.hasPermission(REMOVE_COMMAND)) {
            sender.message(NO_PERMISSION)
            return true
        }

        if(!sender.isPlayer()) {
            sender.message(ONLY_PLAYERS)
            return true
        }
        val p = sender.toPlayer()

        val name = args[0]
        if (getKitByName(name) == null) {
            sender.message(KIT_NOT_EXIST)
            return true
        }
        val k = getKitByName(name)!!
        val e = PlayerRemoveKitEvent(p, k)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return true
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
        sender.message("&cКит &6$name &cуспешно удалён")

        return true
    }
}