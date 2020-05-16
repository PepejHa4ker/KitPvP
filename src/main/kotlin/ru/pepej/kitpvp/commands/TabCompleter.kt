package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.commands.CommandManager.Companion.getSubCommands
import ru.pepej.kitpvp.commands.subcommands.ApplyChangesCommand
import ru.pepej.kitpvp.utils.COMMANDS_PERMISSION

class TabCompleter : TabCompleter {

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if(!sender.hasPermission("$COMMANDS_PERMISSION.kit")) {
            return null
        }
        if(args.size == 1) {
            val toPrint = mutableListOf<String>()
            for(cmd in getSubCommands()) {
                if(sender.hasPermission(cmd.permission)) {
                    toPrint.add(cmd.name)
                }
            }
            return toPrint
        }


        if (args.size == 2) {
            val kitsName = mutableListOf<String>()
            for(cmd in getSubCommands()) {
                if(args[0].equals(cmd.name,true)) {
                    if(!cmd.tabCompletable) {
                        return null
                    }
                }
            }
            if (args[1] != "") {
                for (k in kit.iterator()) {
                    if (k.kitName.startsWith(args[1].toLowerCase())) {
                        kitsName.add(k.kitName)
                    }
                }
            } else {
                for (k in kit.iterator()) {
                    kitsName.add(k.kitName)
                }
            }
            kitsName.sort()
            return kitsName
        }

        return null
    }
}