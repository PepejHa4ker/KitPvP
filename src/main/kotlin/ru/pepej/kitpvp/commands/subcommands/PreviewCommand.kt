@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.getPlayerMenuUtility
import ru.pepej.kitpvp.menu.PreviewMenu
import ru.pepej.kitpvp.model.KitManager.getKitByName
import ru.pepej.kitpvp.utils.*

class PreviewCommand
    : SubCommand(
    name = "preview",
    description = "Предпросмотр кита",
    type = CommandType.KITS
) {
    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {
        if(args isLowerThan 2) {
            sender.message(NOT_ENOUGH_ARGS)
            return
        }
        if(sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        val kit = getKitByName(args[1]) ?: return player.message(KIT_NOT_EXIST)
        PreviewMenu(kit, getPlayerMenuUtility(player)).open()
    }
}