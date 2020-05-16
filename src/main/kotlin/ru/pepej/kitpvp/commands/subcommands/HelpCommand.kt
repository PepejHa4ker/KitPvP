package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.commands.CommandManager.Companion.getSubCommands
import ru.pepej.kitpvp.utils.*

class HelpCommand : SubCommand("help", "$COMMANDS_PERMISSION.help", "Помощь", "/kits help", "h", false) {
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