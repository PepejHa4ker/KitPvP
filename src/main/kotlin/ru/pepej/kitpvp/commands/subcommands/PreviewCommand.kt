@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.getPlayerMenuUtility
import ru.pepej.kitpvp.kit.KitManager
import ru.pepej.kitpvp.menusystem.PreviewMenu
import ru.pepej.kitpvp.utils.COMMANDS_PERMISSION
import ru.pepej.kitpvp.utils.KIT_NOT_EXIST
import ru.pepej.kitpvp.utils.NOT_ENOUGH_ARGS
import ru.pepej.kitpvp.utils.message

class PreviewCommand : SubCommand("preview", "$COMMANDS_PERMISSION.preview", "Предпросмотр кита", "/kits preview <Кит>", "p", true) {

    override fun execute(player: Player, args: Array<out String>) {
        if(args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
        val kit = KitManager.getKitByName(kitName)
        if (kit != null) {
            PreviewMenu(kit, getPlayerMenuUtility(player)).open()
        } else {
            player.message(KIT_NOT_EXIST)
        }
    }

}