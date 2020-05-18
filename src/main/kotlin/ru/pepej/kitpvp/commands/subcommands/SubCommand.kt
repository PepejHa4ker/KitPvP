package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.command.CommandSender
import ru.pepej.kitpvp.utils.COMMANDS_PERMISSION

abstract class SubCommand(
    val name: String,
    val permission: String = "$COMMANDS_PERMISSION.kits.$name",
    val description: String,
    val syntax: String = "/kits $name <Кит>",
    val alias: String = "${name[0]}",
    val type: CommandType
) {
    abstract fun onSubCommand(sender: CommandSender, args: Array<out String>)
}