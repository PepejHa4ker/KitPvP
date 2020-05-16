package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player
import ru.pepej.kitpvp.utils.COMMANDS_PERMISSION

abstract class SubCommand(
    val name: String,
    val permission: String = "$COMMANDS_PERMISSION.kits.$name",
    val description: String,
    val syntax: String,
    val alias: String,
    val tabCompletable: Boolean
) {

    abstract fun execute(player: Player, args: Array<out String>)
}