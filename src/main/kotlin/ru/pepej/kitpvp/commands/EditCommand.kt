package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerEditKitEvent
import ru.pepej.kitpvp.kit.Kit
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.utils.*
import java.util.*
import kotlin.collections.HashMap

class EditCommand : CommandExecutor {
    companion object {
        val editingKit = HashMap<UUID, Kit>()
    }
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
        if(!p.hasPermission(EDIT_COMMAND)) {
            p.message(NO_PERMISSION)
            return true
        }
        if(args.isEmpty()) {
            p.message(NOT_ENOUGH_ARGS)
            return true
        }

        val name = args[0]
        if (getKitByName(name) == null) {
            sender.message(KIT_NOT_EXIST)
            return true
        }
        val kit = getKitByName(name)!!
        val e = PlayerEditKitEvent(p,kit)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return true
        }
        editingKit[p.uniqueId] = kit
        p.inventory.clear()
        p.activePotionEffects.forEach { p.removePotionEffect(it.type) }
        kit.item.forEach { p.inventory.addItem(it) }
        kit.effects.forEach { p.addPotionEffect(it) }
        p.message("&cСейчас Вы можете изменить конфигурацию кита.")
        return true
    }

}