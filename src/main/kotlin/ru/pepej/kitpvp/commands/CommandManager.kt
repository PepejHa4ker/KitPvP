@file:Suppress("UNCHECKED_CAST")

package ru.pepej.kitpvp.commands

import br.com.devsrsouza.kotlinbukkitapi.extensions.hasPermissionOrStar
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.commands.subcommands.*
import ru.pepej.kitpvp.utils.COMMANDS_PERMISSION
import ru.pepej.kitpvp.utils.NO_PERMISSION
import ru.pepej.kitpvp.utils.message
import ru.pepej.kitpvp.utils.toPlayer

class CommandManager : TabExecutor {

    companion object {
        private val subCommands = mutableListOf<SubCommand>()
        fun getSubCommands(): MutableList<SubCommand> {
            return subCommands
        }
    }

    init {
        subCommands.add(ApplyChangesCommand())
        subCommands.add(CreateCommand())
        subCommands.add(EditCommand())
        subCommands.add(HelpCommand())
        subCommands.add(RemoveCommand())
        subCommands.add(PreviewCommand())
        subCommands.add(KitCommand())
        subCommands.add(StatsCommand())
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("$COMMANDS_PERMISSION.kits")) {
            sender.message(NO_PERMISSION)
            return true
        }
        for (i in getSubCommands().withIndex()) {
            when {
                args.isEmpty() -> {
                    sender.message("&cВведите /kits help для получения списка доступных Вам команд.")
                    return true
                }
                args[0].equals(getSubCommands()[i.index].name, true)
                        || args[0].equals(getSubCommands()[i.index].alias, true) -> {
                    if (!sender.hasPermissionOrStar(getSubCommands()[i.index].permission)) {
                        sender.message(NO_PERMISSION)
                        return true
                    }
                    getSubCommands()[i.index].onSubCommand(sender.toPlayer(), args)

                    return true
                }
            }
        }
        return true
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (!sender.hasPermission("$COMMANDS_PERMISSION.kits")) {
            return null
        }
        if (args.size == 1) {
            val toPrint = mutableListOf<String>()
            for (cmd in getSubCommands()) {
                if (sender.hasPermission(cmd.permission)) {
                    toPrint.add(cmd.name)
                }
            }
            return toPrint
        }


        if (args.size == 2) {
            val kitsName = mutableListOf<String>()
            for (cmd in getSubCommands()) {
                if (args[0].equals(cmd.name, true) || args[0].equals(cmd.alias, true)) {
                    if (cmd.type != CommandType.KITS) {
                        return null
                    }
                }
            }
            if (args[1] != "") {
                for (k in KitPvPCore.kit.iterator()) {
                    if (k.kitName.startsWith(args[1].toLowerCase())) {
                        kitsName.add(k.kitName)
                    }
                }
            } else {
                for (k in KitPvPCore.kit.iterator()) {
                    kitsName.add(k.kitName)
                }
            }
            kitsName.sort()
            return kitsName
        }

        return null
    }

}
