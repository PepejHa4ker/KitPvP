@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package ru.pepej.kitpvp.commands

import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.MenuDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.menu
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.newSlot
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.headFromBase64
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.meta
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Item
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import ru.pepej.kitpvp.kit.KitManager
import ru.pepej.kitpvp.utils.*



class PreviewCommand : CommandExecutor {


    companion object {
        var previewMenu: Inventory? = null
        val leave = item(Material.DIAMOND).meta<ItemMeta> { displayName = +"&bВыйти из меню предпросмотра" }

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
        val player = sender.toPlayer()
        if (!player.hasPermission(PREVIEW_COMMAND)) {
            player.message(NO_PERMISSION)
            return true
        }


        if (args.isEmpty()) {
            player.message(NOT_ENOUGH_ARGS)
            return true
        }

        val kitName = args[0]
        val kit = KitManager.getKitByName(kitName)
        if (kit != null) {
            previewMenu = Bukkit.createInventory(null, 54, +"&cПредпросмотр кита ${kit.formattedName}")
            previewMenu!!.add(kit.item)
            val item = item(Material.POTION).meta<PotionMeta> {
                displayName = +"&cЭффекты класса ${kit.formattedName}"
                kit.effects.stream().forEach {
                    addCustomEffect(it, false)
                }
            }
            previewMenu!!.setItem(36, item)
            previewMenu!!.setItem(53, leave)
            player.openInventory(previewMenu)

        } else {
            player.message(KIT_NOT_EXIST)
        }
        return true
    }


}