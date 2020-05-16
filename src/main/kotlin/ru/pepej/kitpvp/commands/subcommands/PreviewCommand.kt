@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.commands.subcommands

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.PotionMeta
import ru.pepej.kitpvp.kit.KitManager
import ru.pepej.kitpvp.utils.*

class PreviewCommand : SubCommand("preview", "$COMMANDS_PERMISSION.preview", "Предпросмотр кита", "/kits preview <Кит>", "p", true) {

    companion object {
        var previewMenu: Inventory? = null
        val leave = item(Material.DIAMOND) { displayName = +"&bВыйти из меню предпросмотра" }
    }
    override fun execute(player: Player, args: Array<out String>) {
        if(args.size < 2) {
            player.message(NOT_ENOUGH_ARGS)
            return
        }
        val kitName = args[1]
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
            previewMenu!!.setItem(53,
                leave
            )
            player.openInventory(previewMenu)

        } else {
            player.message(KIT_NOT_EXIST)
        }
    }

}