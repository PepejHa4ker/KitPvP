@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.listeners

import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.onlinePlayers
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.click
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import de.likewhat.customheads.CustomHeads
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.pepej.kitpvp.BlidnessArena
import ru.pepej.kitpvp.KitPvPCore.Companion.currentKit
import ru.pepej.kitpvp.KitPvPCore.Companion.playerData
import ru.pepej.kitpvp.KitPvPCore.Companion.plugin
import ru.pepej.kitpvp.KitPvPCore.Companion.timesPlayed
import ru.pepej.kitpvp.api.events.player.PlayerKilledByPlayerEvent
import ru.pepej.kitpvp.api.events.server.ServerUpdateEvent
import ru.pepej.kitpvp.api.events.server.UpdateType
import ru.pepej.kitpvp.commands.PreviewCommand.Companion.leave
import ru.pepej.kitpvp.commands.PreviewCommand.Companion.previewMenu
import ru.pepej.kitpvp.kit.KitManager.getKitByName
import ru.pepej.kitpvp.listeners.Listener.Companion.arenas
import ru.pepej.kitpvp.listeners.Listener.Companion.isShootIsBlackHole
import ru.pepej.kitpvp.listeners.Listener.Companion.snb
import ru.pepej.kitpvp.tasks.BlidnessArenaTask
import ru.pepej.kitpvp.tasks.BombTask
import ru.pepej.kitpvp.tasks.HookTask
import ru.pepej.kitpvp.tasks.PullTask
import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.UserManager
import ru.pepej.kitpvp.user.UserManager.loadStatistics
import ru.pepej.kitpvp.utils.message
import java.util.*
import kotlin.collections.HashMap

class EventListener : Listener {

    companion object {
        val picked = HashMap<Player, Hologram>()
        val pickedLoc = HashMap<Player, Location>()
        val isForceActivated = HashMap<Player, Boolean>()
        val bombCd = HashMap<UUID, Long>()
        val evilCd = HashMap<UUID, Long>()
        val hookCd = HashMap<UUID, Long>()
        val evilEyeItem = item(Material.IRON_INGOT, 1) {
            displayName = +"&8Сглазить всех.."
            lore = listOf(
                +"&7Проклятие сглаза I",
                +"&6(Позволяет Вам создавать арену радиусом 15 блоков, любой кто двинется в ней получит слепоту на минуту)")
        }

    val hookItem = item(Material.TRIPWIRE_HOOK, 1) {
        displayName = +"&8Хук полбу стук."
        lore = listOf(+"&7Притяжение I", +"&6(Позволяет Вам притянуть людей в радиусе 8 блоков)")
    }
}

@EventHandler
fun onBlackHoleClick(e: PlayerInteractEvent) {
    val p = e.player
    if (snb.containsKey(p)) {
        val bhHolo = HologramsAPI.createHologram(plugin, snb[p]!!.location.add(0.0, 1.5, 0.0))
        bhHolo.appendTextLine(+"&0&lЧёрная дыра")
        bhHolo.appendTextLine("")
        bhHolo.appendTextLine("")
        CustomHeads.getCategoryManager().getCategory("6").heads.filter { it.id == 60 }.forEach {
            bhHolo.appendItemLine(it.plainItem)
        }
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
            bhHolo.delete()
        }, 60)
        PullTask(p, snb[p]!!.location, 0.5, 100).runTaskTimer(plugin, 0, 1)
        snb[p]!!.remove()
        snb.remove(p)
        isShootIsBlackHole[p] = false
        p.message("&cВы успешно активировали Чёрную Дыру")
    }
}

@EventHandler
fun onBombClick(e: PlayerInteractEvent) {
    val item = e.item ?: return
    val p = e.player
    if (item.itemMeta.displayName == +"&c&lУстановить мину") {
        if (e.clickedBlock != null) {
            if (bombCd.containsKey(p.uniqueId)) {
                val secondsLeft =
                    (bombCd[p.uniqueId]!! / 1000 + 120) - (System.currentTimeMillis() / 1000)
                if (secondsLeft > 0) {
                    p.message("&cОсталось &6$secondsLeft &cсекунд.")
                    e.isCancelled = true
                    return
                }
            }
            bombCd[p.uniqueId] = System.currentTimeMillis()
            p.message("&cВы успешно использовали Мину")
            p.message("&cАллах-Акбар!")
            val bhHolo = HologramsAPI.createHologram(plugin, e.clickedBlock.location.add(0.5, 1.8, 0.5))
            picked[p] = bhHolo
            bhHolo.appendTextLine(+"&6&lМина")
            CustomHeads.getCategoryManager().getCategory("6").heads.filter { it.id == 60 }.forEach {
                bhHolo.appendItemLine(it.plainItem)
            }
            isForceActivated[p] = false
            BombTask(100, p).runTaskTimer(plugin, 0, 1)
        } else {
            p.message("&cВы должны кликать по блоку для установки мины.")
        }
        e.isCancelled = true
    }
}

@EventHandler
fun onBombActivate(e: PlayerInteractEvent) {
    val item = e.item ?: return
    val p = e.player
    if (item.itemMeta.displayName == +"&6&lАктивировать мину") {
        if (picked.containsKey(p)) {
            isForceActivated[p] = true
        } else {
            p.message("&cВы не поставили мину!")
            return
        }
        e.isCancelled = true
    }
}

@EventHandler
fun onSavePointClick(e: PlayerInteractEvent) {
    val item = e.item ?: return
    val p = e.player
    if (item.itemMeta.displayName == +"&bУстановить точку здесь") {
        pickedLoc[p] = p.location
        p.message("&cВы успешно установили точку здесь")
        e.isCancelled = true
    } else if (item.itemMeta.displayName == +"&bТп к точке") {
        if (pickedLoc.containsKey(p)) {
            p.teleport(pickedLoc[p]!!)
            p.message("&cВы были телепортированны к своей точке")
            pickedLoc.remove(p)
        } else {
            p.message("&cУ вас не установленно точек!")

        }
        e.isCancelled = true
    }
}

@EventHandler
fun onEvilEyeClick(e: PlayerInteractEvent) {
    val i = e.item ?: return
    val p = e.player
    if (i == evilEyeItem) {
        if (evilCd.containsKey(p.uniqueId)) {
            val secondsLeft =
                (evilCd[p.uniqueId]!! / 1000 + 3600) - (System.currentTimeMillis() / 1000)
            if (secondsLeft > 0) {
                p.message("&cОсталось &6$secondsLeft &cсекунд.")
                e.isCancelled = true
                return
            }
        }
        evilCd[p.uniqueId] = System.currentTimeMillis()
        val a = BlidnessArena(p.world, p.location.x, p.location.y, p.location.z, 15.0)
        arenas.add(a)
        BlidnessArenaTask(a).runTaskTimer(plugin, 0, 20)
        p.message("&cВы успешно активировали &8Сглаз-Арену")
    }
}

@EventHandler
fun onHookClick(e: PlayerInteractEvent) {
    val i = e.item ?: return
    val p = e.player
    if (i == hookItem) {
        if (hookCd.containsKey(p.uniqueId)) {
            val secondsLeft =
                (hookCd[p.uniqueId]!! / 1000 + 60) - (System.currentTimeMillis() / 1000)
            if (secondsLeft > 0) {
                p.message("&cОсталось &6$secondsLeft &cсекунд.")
                e.isCancelled = true
                return
            }
        }
        hookCd[p.uniqueId] = System.currentTimeMillis()
        val nearPlayers = p.getNearbyEntities(8.0, 8.0, 8.0).stream().filter { it is Player }
        nearPlayers.forEach {
            HookTask(it as Player, p.location, 1.5, 1).runTaskTimer(plugin, 0, 20)
        }
    }
}


@EventHandler
fun onJoin(e: PlayerLoginEvent) {
    loadStatistics(UserManager.getUser(e.player))
    if (playerData.getString("${e.player.uniqueId}.timesplayed").isNullOrEmpty()) {
        playerData.set("${e.player.uniqueId}.timesplayed", 0)
        playerData.save()
    } else timesPlayed[e.player.uniqueId] = playerData.getInt("${e.player.uniqueId}.timesplayed")
    if (playerData.getString("${e.player.uniqueId}.lastclass") != null) {
        val kit = getKitByName(playerData.getString("${e.player.uniqueId}.lastclass"))!!
        currentKit[e.player.uniqueId] = kit

    }
}

@EventHandler
fun onLeave(e: PlayerQuitEvent) {
    if (timesPlayed.contains(e.player.uniqueId)) {
        playerData.set("${e.player.uniqueId}.timesplayed", timesPlayed[e.player.uniqueId])
        playerData.save()
        timesPlayed.remove(e.player.uniqueId)
    }
    if (currentKit.contains(e.player.uniqueId)) {
        playerData.set("${e.player.uniqueId}.lastclass", currentKit[e.player.uniqueId]!!.kitName)
        playerData.save()
        currentKit.remove(e.player.uniqueId)
    }
}

@EventHandler
fun onUpdate(e: ServerUpdateEvent) {
    if (e.type != UpdateType.SECOND) return
    for (p in onlinePlayers()) {
        if (timesPlayed.contains(p.uniqueId)) {
            timesPlayed[p.uniqueId] = timesPlayed[p.uniqueId]!! + 1
        }
    }
}

@EventHandler
fun onPreviewClick(e: InventoryClickEvent) {
    val p = e.whoClicked as? Player? ?: return
    if (e.inventory != null && e.view.topInventory != null) {
        if (e.view.topInventory == previewMenu) {
            e.isCancelled = true
            if (e.currentItem != null && e.currentItem == leave) {
                if (e.isShiftClick || e.click == ClickType.NUMBER_KEY || e.click == ClickType.SHIFT_LEFT || e.click == ClickType.SHIFT_RIGHT) {
                    return
                }
                p.closeInventory()
            }
        }
    }
}


@EventHandler
fun onDeath(e: PlayerDeathEvent) {
    if (currentKit.contains(e.entity.uniqueId)) {
        if (e.entity.killer != null) {
            val damager = e.entity.killer
            val u = UserManager.getUser(damager)
            u.addStat(StatType.KILLS, 1)
            u.addStat(StatType.KILLSTREAK, 1)
            val event = PlayerKilledByPlayerEvent(e.entity, currentKit[e.entity.uniqueId]!!, damager)
            plugin.server.pluginManager.callEvent(event)
            if (event.isCancelled) {
                return
            }
        }
        val u2 = UserManager.getUser(e.entity)
        u2.addStat(StatType.DEATHS, 1)
        u2.setStat(StatType.KILLSTREAK, 0)

    }
    currentKit.remove(e.entity.uniqueId)
}
}