package ru.pepej.kitpvp.commands.subcommands

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
    syntax = "/kits edit <Кит>",
    alias = "e",
    tabCompletable = true
) {
    companion object {
        val editingKit = HashMap<UUID, Kit>()
    }

    override fun execute(player: Player, args: Array<out String>) {
        if(args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }

        val name = args[1]
        if (getKitByName(name) == null) {
            player.message(KIT_NOT_EXIST)
            return
        }
        val kit = getKitByName(name)!!
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