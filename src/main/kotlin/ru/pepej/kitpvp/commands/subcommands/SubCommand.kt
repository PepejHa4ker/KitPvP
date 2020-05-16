package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.entity.Player

abstract class SubCommand(
    val name: String,
    val permission: String,
    val description: String,
    val syntax: String,
    val alias: String,
    val tabCompletable: Boolean
) {

    abstract fun execute(player: Player, args: Array<out String>)
}