package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
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

class CreateCommand
    : SubCommand(
    name = "create",
    description = "Создать кит",
    syntax = "/kits create <Имя> <Цена> <Задержка(Сек)> <Отображаемое имя>",
    alias = "c",
    tabCompletable =  false
) {
    override fun onSubCommand(player: Player, args: Array<out String>) {
        if (args.size < 4) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
        val cost: Double
        val delay: Long
        try {
            cost = args[2].toDouble()
        } catch (e: NumberFormatException) {
            player.message("&c${args[2]} должно быть дробным числом!")
            return
        }
        try {
            delay = args[3].toLong()
        } catch (e: NumberFormatException) {
            player.message("&c${args[3]} должно быть дробным числом!")
            return
        }

        val displayName = args.drop(4).joinToString(" ")
        val items = player.inventory.contents.toList().filterNotNull() as ArrayList<ItemStack>
        val effects = player.activePotionEffects as ArrayList<PotionEffect>
        if (getKitByName(kitName) != null) {
            player.message("&cДанный кит уже существует!")
            return
        }

        val k = Kit(kitName, cost, items, effects, displayName, delay)
        val e = PlayerCreateKitEvent(player, k)
        plugin.server.pluginManager.callEvent(e)
        if (e.isCancelled) {
            return
        }

        kitConfig.set(kitName, kitName)
        kitConfig.set("$kitName.price", cost)
        kitConfig.set("$kitName.delay", delay)
        kitConfig.set("$kitName.items", items)
        kitConfig.set("$kitName.effects", effects)
        kitConfig.set("$kitName.displayname", displayName)
        kitConfig.save()
        player.hover(
            "&cКит: &6$kitName &cСоздан! Наведите для получения полной информации.",
            "&cКит: &6$kitName\n" +
                    "&cЦена: &6$cost\n" +
                    "&cПредметов: &6${items.size}\n" +
                    "&cЭффектов: &6${effects.size}\n" +
                    "&cЗадержка: &6$delay\n" +
                    "&cИмя: &6$displayName"
        )
        kit.add(k)
    }
}