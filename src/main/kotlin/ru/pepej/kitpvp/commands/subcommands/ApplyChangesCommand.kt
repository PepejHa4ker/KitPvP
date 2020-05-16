package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerApplyChangesEvent
import ru.pepej.kitpvp.commands.subcommands.EditCommand.Companion.editingKit
import ru.pepej.kitpvp.kit.KitManager.setupKits
import ru.pepej.kitpvp.utils.*

class ApplyChangesCommand
    : SubCommand(
    name = "apply",
    description =  "Применить изменения",
    syntax =  "/kits apply",
    alias = "a",
    tabCompletable = false
) {

    override fun onSubCommand(player: Player, args: Array<out String>) {
        if (editingKit.contains(player.uniqueId)) {
            val k = editingKit[player.uniqueId]!!
            val e = PlayerApplyChangesEvent(player, k)
            plugin.server.pluginManager.callEvent(e)
            if (e.isCancelled) {
                return
            }
            KitPvPCore.kitConfig.set(
                "${k.kitName}.items",
                player.inventory.contents.toList().filterNotNull() as ArrayList<ItemStack>
            )
            KitPvPCore.kitConfig.set("${k.kitName}.effects", player.activePotionEffects as ArrayList<PotionEffect>)
            KitPvPCore.kitConfig.save()
            player.inventory.clear()
            player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
            KitPvPCore.kitConfig.save()
            kit.remove(k)
            setupKits()
            editingKit.remove(player.uniqueId)
            player.message("&cИзменения были успешно применены!")
        } else {
            player.message("&cНет выбранного кита для изменения!")
        }


    }
}
