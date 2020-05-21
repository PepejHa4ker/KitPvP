@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.utils


import com.pepej.spigotApi.api.util.TimeUtil.formatTime
import com.pepej.spigotApi.api.util.extensions.sendMessage
import com.pepej.spigotApi.api.util.extensions.showText
import com.pepej.spigotApi.api.util.extensions.unaryPlus
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.exceptions.KitPvPException
import ru.pepej.kitpvp.model.Kit

fun Player.hover(chatMessage: String, hoverMessage : String) = this.sendMessage(TextComponent(+"&7[&6Kit&cPvP&7] $chatMessage").showText(TextComponent(+hoverMessage)))
fun CommandSender.message(message: String) = sendMessage(+"&7[&6Kit&cPvP&7] $message")
fun CommandSender.message(vararg message: String) = message.forEach { this.message(it) }
fun CommandSender.toPlayer(): Player = this as? Player ?: throw KitPvPException("This type cannot be cast to player!")
fun Player.getKitByPlayer(): Kit? {
    return currentKit[this.uniqueId] ?: return null
}
fun Int.toDateFormat() = formatTime(this)
infix fun Array<out String>.isLowerThan(size: Int): Boolean = this.size < size
fun Listener.registerEvents(plugin: Plugin)
        = plugin.server.pluginManager.registerEvents(this, plugin)










