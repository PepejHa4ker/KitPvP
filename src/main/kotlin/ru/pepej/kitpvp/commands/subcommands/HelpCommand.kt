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
    override fun execute(player: Player, args: Array<out String>) {

        player.message(
            "          &cKit&6PvP",
            "          &6Author:&a pepej"
        )
        getSubCommands().forEach {
            val toPrint = mutableListOf<String>()
            if(player.hasPermission(it.permission)) {
                toPrint.add("&c${it.syntax}&7(${it.alias}) &6- &c${it.description}")
            }
            toPrint.forEach { msg -> player.message(msg) }
        }
    }
}