@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.click
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.runCommand
import br.com.devsrsouza.kotlinbukkitapi.plugins.vault.economy
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerChangeKitEvent
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.kit.KitManager.kitDelay
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.utils.*
import ru.pepej.kitpvp.utils.TimeUtil.formatTime

class KitCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if(!sender.hasPermission("$COMMANDS_PERMISSION.kit")) {
            sender.message("&cНет прав!")
            return true
        }
        if (args.isEmpty()) {
            sender.message(NOT_ENOUGH_ARGS)
            return true
        }
        val kitName = args[0]
        val kit = getKitByName(kitName)
        if (kit != null) {
            if (args.size == 1) {
                if(!sender.isPlayer()) {
                    sender.message(ONLY_PLAYERS)
                    return true
                }
                val player = sender.toPlayer()
                if (!player.hasPermission("$COMMANDS_PERMISSION.kit.himself")) {
                    sender.message("&cНет прав!")
                    return true
                }
                if (economy!!.getBalance(player) > kit.cost) {
                    if (kitDelay.contains(kit)) {
                        val secondsLeft =
                            (kitDelay[kit]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                        if (secondsLeft > 0) {
                            player.message("&cОсталось&6${formatTime(secondsLeft.toInt())}")
                            return true
                        }
                    }
                    kitDelay[kit] = System.currentTimeMillis()
                    val event = PlayerChangeKitEvent(player, kit)
                    plugin.server.pluginManager.callEvent(event)
                    if (event.isCancelled) {
                        return true
                    }
                    val u = UserManager.getUser(player)
                    u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                    player.hover(
                        "&cВы успешно взяли кит ${kit.formattedName}",
                        "&cЦена кита: &6${if(kit.cost > 0) "${kit.cost}" else "Бесплатно!"}\n" +
                                "&cВещей в ките: &6${if(kit.item.size > 0) "${kit.item.size}" else "Пусто!"}\n" +
                                "&cЭффектов в ките: &6${if(kit.effects.size > 0) "${kit.effects.size}" else "Пусто!"}\n" +
                                "&cЗадержка:&6${if(kit.delay > 0) formatTime(kit.delay.toInt()) else " Без задержки!"}")

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
                    economy!!.withdrawPlayer(player, kit.cost)
                    KitPvPCore.playerData.set("${player.uniqueId}.lastclass", kit.kitName)
                    KitPvPCore.playerData.save()
                } else {
                    player.message("&cУ вас недостаточно средств.")
                }
            } else if (args.size == 2) {
                if (!sender.hasPermission("$COMMANDS_PERMISSION.kit.other")) {
                    sender.message("&cНет прав!")
                    return true
                }
                val player  = plugin.server.getPlayer(args[1]) ?: return true
                if (economy!!.getBalance(player) > kit.cost) {
                    if (kitDelay.contains(kit)) {
                        val secondsLeft =
                            (kitDelay[kit]!! / 1000 + kitConfig.getLong("$kitName.delay")) - (System.currentTimeMillis() / 1000)
                        if (secondsLeft > 0) {
                            player.message("&cОсталось &6$secondsLeft &cсекунд.")
                            return true
                        }
                    }
                    kitDelay[kit] = System.currentTimeMillis()
                    val event = PlayerChangeKitEvent(player, kit)
                    plugin.server.pluginManager.callEvent(event)
                    if (event.isCancelled) {
                        return true
                    }
                    val u = UserManager.getUser(player)
                    u.setStat(StatType.KITS_PICKED, u.getStat(StatType.KITS_PICKED)!! + 1)
                    player.message("&cВы успешно взяли кит ${kit.formattedName}, &cцена: &6${kit.cost}")
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
                    economy!!.withdrawPlayer(player, kit.cost)
                    KitPvPCore.playerData.set("${player.uniqueId}.lastclass", kit.kitName)
                    KitPvPCore.playerData.save()
                } else {
                    player.message("&cУ вас недостаточно средств.")
                }
            }
        } else {
            sender.message("&cДанного кита не существует.")

        }
        return true
    }

}