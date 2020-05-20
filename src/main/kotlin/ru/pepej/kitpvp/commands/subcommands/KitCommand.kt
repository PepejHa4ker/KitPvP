@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands


import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.KitPvPCore.Companion.economy
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.playerData
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerChangeKitEvent
import ru.pepej.kitpvp.model.KitManager.getKitByName
import ru.pepej.kitpvp.model.KitManager.kitDelay
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.utils.*
import ru.pepej.kitpvp.utils.TimeUtil.formatTime

class KitCommand
    : SubCommand(
    name = "give",
    description = "Взять кит",
    syntax = "/kits give <Кит> [Игрок]",
    type = CommandType.KITS
) {


    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {

        if(sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        if (args isLowerThan 2) {
            sender.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
        val k = getKitByName(kitName) ?: return player.message(KIT_NOT_EXIST)
        if (args.size == 2) {
            if (economy.getBalance(player) > k.cost) {
                if (kitDelay.contains(k)) {
                    val secondsLeft =
                        (kitDelay[k]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                    if (secondsLeft > 0) {
                        player.message("&cОсталось&6${formatTime(secondsLeft.toInt())}")
                        return
                    }
                }
                kitDelay[k] = System.currentTimeMillis()
                val event = PlayerChangeKitEvent(player, k)
                plugin.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return
                }
                val u = UserManager.getUser(player)
                u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                player.hover(
                    "&cВы успешно взяли кит ${k.formattedName}",
                    "&cЦена кита: &6${if (k.cost > 0) "${k.cost}" else "Бесплатно!"}\n" +
                            "&cВещей в ките: &6${if (k.item.size > 0) "${k.item.size}" else "Пусто!"}\n" +
                            "&cЭффектов в ките: &6${if (k.effects.size > 0) "${k.effects.size}" else "Пусто!"}\n" +
                            "&cЗадержка:&6${if (k.delay > 0) formatTime(k.delay.toInt()) else " Без задержки!"}"
                )

                player.inventory.clear()
                player.activePotionEffects.forEach {
                    player.removePotionEffect(it.type)
                }
                currentKit[player.uniqueId] = k
                k.item.forEach {
                    player.inventory.addItem(it)
                }
                k.effects.forEach {
                    player.addPotionEffect(it)
                }
                economy.withdrawPlayer(player, k.cost)
                playerData.set("${player.uniqueId}.lastclass", k.kitName)
                playerData.save()
            } else {
                player.message("&cУ вас недостаточно средств.")
            }
        } else if (args.size == 3) {
            val p = plugin.server.getPlayer(args[2]) ?: return
            if (economy.getBalance(p) > k.cost) {
                if (kitDelay.contains(k)) {
                    val secondsLeft =
                        (kitDelay[k]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                    if (secondsLeft > 0) {
                        p.message("&cОсталось &6$secondsLeft &cсекунд.")
                        return
                    }
                }
                kitDelay[k] = System.currentTimeMillis()
                val event = PlayerChangeKitEvent(p, k)
                plugin.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return
                }
                val u = UserManager.getUser(p)
                u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                p.message("&cВы успешно взяли кит ${k.formattedName}, &cцена: &6${k.cost}")
                p.inventory.clear()
                p.activePotionEffects.forEach {
                    p.removePotionEffect(it.type)
                }
                currentKit[p.uniqueId] = k
                k.item.forEach {
                    p.inventory.addItem(it)
                }
                k.effects.forEach {
                    p.addPotionEffect(it)
                }
                economy.withdrawPlayer(p, k.cost)
                playerData.set("${p.uniqueId}.lastclass", k.kitName)
                playerData.save()
            } else {
                p.message("&cУ вас недостаточно средств.")
            }
        }
    }
}
