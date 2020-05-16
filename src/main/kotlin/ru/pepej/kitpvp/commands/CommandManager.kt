package ru.pepej.kitpvp.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.pepej.kitpvp.commands.subcommands.*
import ru.pepej.kitpvp.utils.*

class CommandManager : CommandExecutor {

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

}
