package ru.pepej.kitpvp.menu

import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import com.pepej.spigotApi.api.util.extensions.add
import com.pepej.spigotApi.api.util.extensions.meta
import com.pepej.spigotApi.api.util.extensions.unaryPlus
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.PotionMeta
import ru.pepej.kitpvp.model.Kit


class PreviewMenu(
    private val kit: Kit,
    playerMenuUtility: PlayerMenuUtility
): Menu(
    "&cПредпросмотр кита ${kit.formattedName}",
    54,
    playerMenuUtility
) {

    companion object {
        val leave = item(Material.DIAMOND) { displayName = +"&bВыйти из меню предпросмотра" }
    }

    override fun handleMenu(e: InventoryClickEvent) {
        val p = e.whoClicked as? Player? ?: return
        if(e.currentItem == leave) {
            if(e.click == ClickType.SHIFT_RIGHT || e.click == ClickType.NUMBER_KEY) return
            p.closeInventory()
        }
    }

    override fun setMenuItems() {
        inventory.add(kit.item)
        val item = item(Material.POTION).meta<PotionMeta> {
            displayName = +"&cЭффекты класса ${kit.formattedName}"
            kit.effects.stream().forEach {
                addCustomEffect(it, false)
            }
        }
        inventory.setItem(36, item)
        inventory.setItem(53, leave)
    }

}