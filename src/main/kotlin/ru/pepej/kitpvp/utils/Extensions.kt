@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.utils

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.sendMessage
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.kit.Kit
import ru.pepej.kitpvp.utils.TimeUtil.formatTime
import java.lang.NullPointerException

fun CommandSender.message(message: String) = this.sendMessage(+"&7[&6Kit&cPvP&7] $message")
fun CommandSender.message(vararg message: String) = message.forEach { this.message(it) }
fun CommandSender.toPlayer(): Player = this as? Player ?: throw NullPointerException("This type cannot be cast to player!")
fun CommandSender.isPlayer(): Boolean = this is Player
fun Player.getKitByPlayer(): Kit? {
    currentKit[this.uniqueId] ?: return null
    return currentKit[this.uniqueId]!!
}
fun Player.hover(chatMessage: String, hoverMessage : String) = this.sendMessage(TextComponent(+"&7[&6Kit&cPvP&7] $chatMessage").showText(TextComponent(+hoverMessage)))
inline fun <reified T : ItemMeta> ItemStack.meta(task: T.() -> Unit): ItemStack = apply { itemMeta = (itemMeta as? T)?.apply(task) ?: itemMeta }
inline fun item(material: Material, amount: Int = 1, data: Short = 0, meta: ItemMeta.() -> Unit = {}): ItemStack = ItemStack(material, amount, data).meta(meta)
fun Inventory.add(items: ArrayList<ItemStack>): Unit = items.forEach { this.addItem(it) }
fun String.translateColor(code: Char = '&'): String = ChatColor.translateAlternateColorCodes(code, this)
fun <T : BaseComponent> T.hover(hoverEvent: HoverEvent) = apply { this.hoverEvent = hoverEvent }
fun <T : BaseComponent> T.showText(component: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
operator fun String.unaryPlus(): String = translateColor()
operator fun String.unaryMinus() = replace('§', '&')
fun Int.toDateFormat() = formatTime(this)


