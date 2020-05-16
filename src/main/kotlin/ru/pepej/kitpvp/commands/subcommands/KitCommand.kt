@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands


import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.KitPvPCore.Companion.economy
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerChangeKitEvent
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.kit.KitManager.kitDelay
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.utils.*
import ru.pepej.kitpvp.utils.TimeUtil.formatTime

class KitCommand
    : SubCommand(
    name = "give",
    description = "Взять кит",
    syntax = "/kits give <Кит> [Игрок]",
    alias = "g",
    tabCompletable = true
) {
    override fun onSubCommand(player: Player, args: Array<out String>) {

        if (args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
        val kit = getKitByName(name) ?: return player.message(KIT_NOT_EXIST)
        if (args.size == 2) {
            if (economy.getBalance(player) > kit.cost) {
                if (kitDelay.contains(kit)) {
                    val secondsLeft =
                        (kitDelay[kit]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                    if (secondsLeft > 0) {
                        player.message("&cОсталось&6${formatTime(secondsLeft.toInt())}")
                        return
                    }
                }
                kitDelay[kit] = System.currentTimeMillis()
                val event = PlayerChangeKitEvent(player, kit)
                plugin.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return
                }
                val u = UserManager.getUser(player)
                u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                player.hover(
                    "&cВы успешно взяли кит ${kit.formattedName}",
                    "&cЦена кита: &6${if (kit.cost > 0) "${kit.cost}" else "Бесплатно!"}\n" +
                            "&cВещей в ките: &6${if (kit.item.size > 0) "${kit.item.size}" else "Пусто!"}\n" +
                            "&cЭффектов в ките: &6${if (kit.effects.size > 0) "${kit.effects.size}" else "Пусто!"}\n" +
                            "&cЗадержка:&6${if (kit.delay > 0) formatTime(kit.delay.toInt()) else " Без задержки!"}"
                )

                player.inventory.clear()
                player.activePotionEffects.forEach {
                    player.removePotionEffect(it.type)
                }
                KitPvPCore.currentKit[player.uniqueId] = kit
                kit.item.forEach {
                    player.inventory.addItem(it)
                }
                kit.effects.forEach {
                    player.addPotionEffect(it)
                }
                economy.withdrawPlayer(player, kit.cost)
                KitPvPCore.playerData.set("${player.uniqueId}.lastclass", kit.kitName)
                KitPvPCore.playerData.save()
            } else {
                player.message("&cУ вас недостаточно средств.")
            }
        } else if (args.size == 3) {
            val p = plugin.server.getPlayer(args[2]) ?: return
            if (economy.getBalance(p) > kit.cost) {
                if (kitDelay.contains(kit)) {
                    val secondsLeft =
                        (kitDelay[kit]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                    if (secondsLeft > 0) {
                        p.message("&cОсталось &6$secondsLeft &cсекунд.")
                        return
                    }
                }
                kitDelay[kit] = System.currentTimeMillis()
                val event = PlayerChangeKitEvent(p, kit)
                plugin.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return
                }
                val u = UserManager.getUser(p)
                u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                p.message("&cВы успешно взяли кит ${kit.formattedName}, &cцена: &6${kit.cost}")
                p.inventory.clear()
                p.activePotionEffects.forEach {
                    p.removePotionEffect(it.type)
                }
                KitPvPCore.currentKit[p.uniqueId] = kit
                kit.item.forEach {
                    p.inventory.addItem(it)
                }
                kit.effects.forEach {
                    p.addPotionEffect(it)
                }
                economy.withdrawPlayer(p, kit.cost)
                KitPvPCore.playerData.set("${p.uniqueId}.lastclass", kit.kitName)
                KitPvPCore.playerData.save()
            } else {
                p.message("&cУ вас недостаточно средств.")
            }
        }
    }
}
