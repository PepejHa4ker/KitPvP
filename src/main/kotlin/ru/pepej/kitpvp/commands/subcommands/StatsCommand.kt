package ru.pepej.kitpvp.commands.subcommands

import com.pepej.spigotApi.api.util.TimeUtil.formatTime
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.utils.*

@Suppress("DEPRECATION")
class StatsCommand
    : SubCommand(
    name = "stats",
    description = "Узнать статистику",
    syntax = "/kits stats [Игрок]",
    type = CommandType.LAST
) {
    override fun onSubCommand(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.message(ONLY_PLAYERS)
            return
        }
        val player = sender.toPlayer()
        when (args.size) {
            1 -> {
                player.message("&cВаша статистика:")
                player.message("&c&m-------------------")
                for (stat in StatType.values()) {
                    player.message("${stat.formattedName}: &6${UserManager.getUser(player).getStat(stat)} ")
                }
                player.message("&cВремени в игре:&6${formatTime(timesPlayed[player.uniqueId]!!)}")
                player.message("&c&m-------------------")
            }
            2 -> {
                val target = Bukkit.getPlayer(args[1]) ?: return sender.message(PLAYER_NOT_FOUND)
                player.message("&cСтатистика игрока&6 ${target.name}:")
                player.message("&c&m-------------------")
                for (stat in StatType.values()) {
                    player.message("${stat.formattedName}: &6${UserManager.getUser(target).getStat(stat)} ")
                }
                player.message("&cВремени в игре:&6$ ${timesPlayed[player.uniqueId]!!.toDateFormat()}")
                player.message("&c&m-------------------")
            }
        }
    }
}