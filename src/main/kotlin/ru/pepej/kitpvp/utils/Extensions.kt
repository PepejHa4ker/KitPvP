@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.utils


import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.model.Kit
import ru.pepej.kitpvp.utils.TimeUtil.formatTime
import java.util.*

fun Player.sendMessage(text: BaseComponent) = spigot().sendMessage(text)
fun CommandSender.message(message: String) = sendMessage(+"&7[&6Kit&cPvP&7] $message")
fun CommandSender.message(vararg message: String) = message.forEach { this.message(it) }
fun CommandSender.toPlayer(): Player = this as? Player ?: throw NullPointerException("This type cannot be cast to player!")
fun Player.getKitByPlayer(): Kit? {
    return currentKit[this.uniqueId] ?: return null
}
//ITEM EXTENSIONS
inline fun <reified T : ItemMeta> ItemStack.meta(block: T.() -> Unit): ItemStack = apply { itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta }
inline fun item(material: Material, amount: Int = 1, data: Short = 0, meta: ItemMeta.() -> Unit = {}): ItemStack = ItemStack(material, amount, data).meta(meta)
fun Inventory.add(items: ArrayList<ItemStack>) = items.forEach { this.addItem(it) }
//BUNGEECORD TEXTCOMPONENT EXTENSIONS
fun Player.hover(chatMessage: String, hoverMessage : String) = this.sendMessage(TextComponent(+"&7[&6Kit&cPvP&7] $chatMessage").showText(TextComponent(+hoverMessage)))
fun <T : BaseComponent> T.hover(hoverEvent: HoverEvent) = apply { this.hoverEvent = hoverEvent }
fun <T : BaseComponent> T.showText(component: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
// PRIMITIVE TYPES EXTENSIONS
operator fun String.unaryPlus(): String = translateColor()
operator fun String.unaryMinus() = replace('§', '&')
fun String.translateColor(code: Char = '&'): String = ChatColor.translateAlternateColorCodes(code, this)

fun Int.toDateFormat() = formatTime(this)
//COLLECTIONS EXTENSIONS
infix fun Array<out String>.isLowerThan(size: Int): Boolean = this.size < size
inline fun <reified T : Player> Collection<T>.findOrNull(predicate: T.() -> Boolean): List<T?>? {
    val toReturn: MutableList<T?> = mutableListOf()
    for (element in this)  {
        if (predicate(element)) toReturn.add(element)
        return toReturn
    }
    return null
}
//------------------------------------------------------------------------------------------------------------------------
// EVENT EXTENSIONS
inline fun <reified T : Event> KListener<*>.event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = true,
    crossinline block: T.() -> Unit
) = event(plugin, priority, ignoreCancelled, block)
inline fun <reified T : Event> Listener.event(
    plugin: Plugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = true,
    crossinline block: T.() -> Unit
) {
    Bukkit.getServer().pluginManager.registerEvent(
        T::class.java,
        this,
        priority,
        {_, e ->
            (e as? T)?.block()
        },
        plugin,
        ignoreCancelled
    )
}
fun Listener.registerEvents(plugin: Plugin)
        = plugin.server.pluginManager.registerEvents(this, plugin)
inline fun Plugin.events(block: KListener<*>.() -> Unit) = InlineKListener(this).apply(block)
interface KListener<T : Plugin> : Listener { val plugin: T }
inline class InlineKListener(override val plugin: Plugin) : KListener<Plugin>










