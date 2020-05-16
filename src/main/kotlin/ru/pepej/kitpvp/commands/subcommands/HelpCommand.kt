package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.commands.CommandManager.Companion.getSubCommands
import ru.pepej.kitpvp.utils.*

class HelpCommand
    : SubCommand(
    name = "help",
    description = "Помощь",
    syntax = "/kits help",
    alias = "h",
    tabCompletable = false
) {
    override fun onSubCommand(player: Player, args: Array<out String>) {

        player.message(
            "          &cKit&6PvP",
            "          &6Author:&a pepej"
        )
        val permittedCommands = getSubCommands().filter { player.hasPermission(it.permission) }
        val toPrint = mutableListOf<String>()
        permittedCommands.forEach { permittedCommand ->
            toPrint.add("&c${permittedCommand.syntax}&7(${permittedCommand.alias}) &6- &c${permittedCommand.description}")
        }
        if (permittedCommands.isEmpty()) {
            player.message("&cУ Вас нет доступных команд.")
            return
        }
        toPrint.forEach { player.message(it) }

    }
}