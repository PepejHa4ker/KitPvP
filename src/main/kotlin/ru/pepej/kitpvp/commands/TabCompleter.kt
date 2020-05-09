package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
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
        if (args.size == 1) {
            val kitsName = mutableListOf<String>()
            if (args[0] != "") {
                for (k in kit.iterator()) {
                    if (k.kitName.startsWith(args[0].toLowerCase())) {
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