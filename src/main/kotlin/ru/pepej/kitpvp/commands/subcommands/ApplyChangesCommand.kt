package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerApplyChangesEvent
import ru.pepej.kitpvp.commands.subcommands.EditCommand.Companion.editingKit
import ru.pepej.kitpvp.model.KitManager.setupKits
import ru.pepej.kitpvp.utils.*

class ApplyChangesCommand
    : SubCommand(
    name = "apply",
    description =  "Применить изменения",
    syntax =  "/kits apply",
    type = CommandType.LAST
) {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {
        if(sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        if (editingKit.contains(player.uniqueId)) {
            val k = editingKit[player.uniqueId]!!
            val e = PlayerApplyChangesEvent(player, k)
            plugin.server.pluginManager.callEvent(e)
            if (e.isCancelled) {
                return
            }
            kitConfig.set(
                "${k.kitName}.items",
                player.inventory.contents.toList().filterNotNull() as ArrayList<ItemStack>
            )
            kitConfig.set("${k.kitName}.effects", player.activePotionEffects as ArrayList<PotionEffect>)
            kitConfig.save()
            player.inventory.clear()
            player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
            kitConfig.save()
            kit.remove(k)
            setupKits()
            editingKit.remove(player.uniqueId)
            player.message("&cИзменения были успешно применены!")
        } else {
            player.message("&cНет выбранного кита для изменения!")
        }


    }
}
