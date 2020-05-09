package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerApplyChangesEvent
import ru.pepej.kitpvp.commands.EditCommand.Companion.editingKit
import ru.pepej.kitpvp.kit.KitManager.setupKits
import ru.pepej.kitpvp.utils.*

class ApplyChangesCommand : CommandExecutor {
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
        if(!p.hasPermission(APPLY_COMMAND)) {
            p.message(NO_PERMISSION)
            return true
        }
        if(editingKit.contains(p.uniqueId)) {
            val k = editingKit[p.uniqueId]!!
            val e = PlayerApplyChangesEvent(p, k)
            plugin.server.pluginManager.callEvent(e)
            if(e.isCancelled) {
                return true
            }
            KitPvPCore.kitConfig.set("${k.kitName}.items", p.inventory.contents.toList().filterNotNull() as ArrayList<ItemStack>)
            KitPvPCore.kitConfig.set("${k.kitName}.effects", p.activePotionEffects as ArrayList<PotionEffect>)
            KitPvPCore.kitConfig.save()
            p.inventory.clear()
            p.activePotionEffects.forEach { p.removePotionEffect(it.type) }
            KitPvPCore.kitConfig.save()
            kit.remove(k)
            setupKits()
            editingKit.remove(p.uniqueId)
            p.message("&cИзменения были успешно применены!")
        } else {
            p.message("&cНет выбранного кита для изменения!")
            return true
        }
        return true
    }

}