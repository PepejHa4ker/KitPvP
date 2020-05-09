package ru.pepej.kitpvp.utils

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.hover
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.sendMessage
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.kit.Kit

fun CommandSender.message(message: String) = this.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&3[&6Kit&cPvP&3] $message"))
fun CommandSender.toPlayer(): Player = this as Player
fun CommandSender.isPlayer(): Boolean = this is Player
fun CommandSender.isConsole(): Boolean = this is ConsoleCommandSender
fun Player.getKitByPlayer(): Kit? {
    if(KitPvPCore.currentKit.contains(this.uniqueId)) return KitPvPCore.currentKit[this.uniqueId]!!
    return null
}
fun Player.hover(chatMessage: String, hoverMessage : String) = this.sendMessage(
    TextComponent(+chatMessage).hover(
        HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(+hoverMessage).create())
    ))
inline fun <reified T : ItemMeta> ItemStack.meta(task: T.() -> Unit): ItemStack = apply { itemMeta = (itemMeta as? T)?.apply(task) ?: itemMeta }
inline fun item(material: Material, amount: Int = 1, data: Short = 0, meta: ItemMeta.() -> Unit = {}): ItemStack = ItemStack(material, amount, data).meta(meta)
fun Inventory.add(items: ArrayList<ItemStack>): Unit = items.forEach { this.addItem(it) }