@file:Suppress("UNCHECKED_CAST")

package ru.pepej.kitpvp.kit

import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.pepej.kitpvp.KitPvPCore.Companion.cs
import ru.pepej.kitpvp.KitPvPCore.Companion.kit
import ru.pepej.kitpvp.KitPvPCore.Companion.kitConfig
import ru.pepej.kitpvp.KitPvPCore.Companion.kitData
import ru.pepej.kitpvp.utils.message

object KitManager {
    val kitDelay = HashMap<Kit, Long>()

    fun setupKits() {
        for (s in kitConfig.getConfigurationSection("").getKeys(false)) {
            if (s != null) {
                cs.message("&cЗагружаю кит '$s'...")
                val k = Kit(s, kitConfig.getDouble("$s.price"),
                    kitConfig.get("$s.items") as ArrayList<ItemStack>,
                    kitConfig.get("$s.effects") as ArrayList<PotionEffect>,
                    kitConfig.getString("$s.displayname"),
                    kitConfig.getLong("$s.delay")
                )
                kit.add(k)
                cs.message("&cКит '$s' загружен!")
            } else {
                cs.message("&cКиты не найдены... Создайте их!")
            }
        }
        for (s in kitData.getConfigurationSection("").getKeys(false)) {
            if(s != null) {
                val k = getKitByName(s)!!
                kitDelay[k] = kitData.getLong("$s.delay")
            }
        }
    }

    fun getKitByName(name: String): Kit? {
        for (k in kit) {
            if (k.kitName.equals(name, true)) {
                return k
            }
        }
        return null
    }

    fun getKits(): HashSet<Kit> {
        return kit
    }

}