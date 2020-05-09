package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerCreateKitEvent
import ru.pepej.kitpvp.kit.Kit
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.utils.*
import java.lang.NumberFormatException

class CreateCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(!sender.isPlayer()) {
            sender.message(ONLY_PLAYERS)
            return true
        }
        val p = sender.toPlayer()
        if(!p.hasPermission(CREATE_COMMAND)) {
            p.message(NO_PERMISSION)
            return true
        }

        if(args.size < 4) {
            p.message(NOT_ENOUGH_ARGS)
            return true
        }
        val kitName = args[0]
        val cost: Double
        val delay: Long
        try {
            cost = args[1].toDouble()
        } catch (e: NumberFormatException) {
            p.message("&c${args[1]} должно быть дробным числом!")
            return true
        }
        try {
            delay = args[2].toLong()
        } catch (e: NumberFormatException) {
            p.message("&c${args[2]} должно быть дробным числом!")
            return true
        }

        val displayName = args.drop(3).joinToString(" ")
        val items = p.inventory.contents.toList().filterNotNull() as ArrayList<ItemStack>
        val effects = p.activePotionEffects as ArrayList<PotionEffect>
        if(getKitByName(kitName) != null) {
            p.message("&cДанный кит уже существует!")
            return true
        }

        val k = Kit(kitName, cost, items, effects, displayName, delay)
        val e = PlayerCreateKitEvent(p, k)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return true
        }

        kitConfig.set(kitName, kitName)
        kitConfig.set("$kitName.price", cost)
        kitConfig.set("$kitName.delay", delay)
        kitConfig.set("$kitName.items", items)
        kitConfig.set("$kitName.effects", effects)
        kitConfig.set("$kitName.displayname", displayName)
        kitConfig.save()
        p.message("&cКит: &6$kitName, &cЦена: &6$cost &cПредметов: &6${items.size}, &cЭффектов: &6${effects.size}, &cЗадержка: &6$delay &cИмя: &6$displayName &cСоздан!")
        kit.add(k)
        return true
    }
}