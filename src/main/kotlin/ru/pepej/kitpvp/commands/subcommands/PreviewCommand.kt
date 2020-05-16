@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.getPlayerMenuUtility
import ru.pepej.kitpvp.kit.KitManager
import ru.pepej.kitpvp.menu.PreviewMenu
import ru.pepej.kitpvp.utils.KIT_NOT_EXIST
import ru.pepej.kitpvp.utils.NOT_ENOUGH_ARGS
import ru.pepej.kitpvp.utils.message

class PreviewCommand
    : SubCommand(
    name = "preview",
    description = "Предпросмотр кита",
    syntax = "/kits preview <Кит>",
    alias = "p",
    tabCompletable = true
) {

    override fun onSubCommand(player: Player, args: Array<out String>) {
        if(args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val kit = KitManager.getKitByName(args[1]) ?: return player.message(KIT_NOT_EXIST)
        PreviewMenu(kit, getPlayerMenuUtility(player)).open()
    }
}