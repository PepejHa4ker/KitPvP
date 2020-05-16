package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.commands.subcommands.*
import ru.pepej.kitpvp.utils.*

class CommandManager : TabExecutor {

    companion object {
        private val subcommands = mutableListOf<SubCommand>()
        fun getSubCommands(): MutableList<SubCommand> {
            return subcommands
        }
    }

    init {
        subcommands.add(ApplyChangesCommand())
        subcommands.add(CreateCommand())
        subcommands.add(EditCommand())
        subcommands.add(HelpCommand())
        subcommands.add(RemoveCommand())
        subcommands.add(PreviewCommand())
        subcommands.add(KitCommand())
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.isPlayer()) {
            sender.message(ONLY_PLAYERS)
            return true
        }
        for (i in getSubCommands().withIndex()) {
            if (args.isEmpty()) {
                sender.message(
                    "          &cKit&6PvP",
                    "          &6Author:&a pepej"
                )
                getSubCommands().forEach {
                    val toPrint = mutableListOf<String>()
                    if(sender.hasPermission(it.permission)) {
                        toPrint.add("&c${it.syntax}&7(${it.alias}) &6- &c${it.description}")
                    }
                    toPrint.forEach { msg -> sender.message(msg) }
                }
                return true
            }
            if (args[0].equals(
                    getSubCommands()[i.index].name,
                    true
                ) || args[0].equals(getSubCommands()[i.index].alias, true)
            ) {
                if (!sender.hasPermission(getSubCommands()[i.index].permission)) {
                    sender.message(NO_PERMISSION)
                    return true
                }
                getSubCommands()[i.index].execute(sender.toPlayer(), args)
                return true
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
