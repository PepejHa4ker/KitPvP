package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
import ru.pepej.kitpvp.commands.CommandManager.Companion.getSubCommands
import ru.pepej.kitpvp.utils.*

class HelpCommand
    : SubCommand(
    name = "help",
    description = "Помощь",
    syntax = "/kits help",
    type = CommandType.LAST
) {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {

        sender.message(
            "          &cKit&6PvP",
            "          &6Author:&a pepej"
        )
        val permittedCommands = getSubCommands().filter { sender.hasPermission(it.permission) }
        val toPrint = mutableListOf<String>()
        permittedCommands.forEach { permittedCommand ->
            toPrint.add("&c${permittedCommand.syntax}&7(${permittedCommand.alias}) &6- &c${permittedCommand.description}")
        }
        if (permittedCommands.isEmpty()) {
            sender.message("&cУ Вас нет доступных команд.")
            return
        }
        toPrint.forEach { sender.message(it) }

    }
}