package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.api.events.player.PlayerEditKitEvent
import ru.pepej.kitpvp.kit.Kit
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.utils.*
import java.util.*
import kotlin.collections.HashMap

class EditCommand
    : SubCommand(
    name = "edit",
    description = "Изменить кит",
    type = CommandType.KITS
) {
    companion object {
        val editingKit = HashMap<UUID, Kit>()
    }

    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {
        if(args.size < 2) {
            sender.message(NOT_ENOUGH_ARGS)
            return
        }

        if(sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        val name = args[1]
        val kit = getKitByName(name) ?: return player.message(KIT_NOT_EXIST)
        val e = PlayerEditKitEvent(player,kit)
        plugin.server.pluginManager.callEvent(e)
        if(e.isCancelled) {
            return
        }
        editingKit[player.uniqueId] = kit
        player.inventory.clear()
        player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
        kit.item.forEach { player.inventory.addItem(it) }
        kit.effects.forEach { player.addPotionEffect(it) }
        player.message("&cСейчас Вы можете изменить конфигурацию кита.")
    }

}