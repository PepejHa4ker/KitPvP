@file:Suppress("DEPRECATION")

package ru.pepej.kitpvp.listeners


import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.events
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.meta
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import ru.pepej.kitpvp.BlidnessArena
import ru.pepej.kitpvp.KitPvPCore
import ru.pepej.kitpvp.utils.message
import ru.pepej.kitpvp.tasks.BlazeRodTask
import ru.pepej.kitpvp.tasks.PullTask
import ru.pepej.kitpvp.tasks.TntTask
import ru.pepej.kitpvp.tasks.TorpedoTask
import java.util.*
import kotlin.collections.HashMap


class Listener(override val plugin: KitPvPCore) : KListener<KitPvPCore> {

    companion object {
        private val blazeCooldown = HashMap<UUID, Long>()
        private val blackHoleCooldown = HashMap<UUID, Long>()
        val snb = HashMap<Player, Snowball>()
        val playersToBoom = HashMap<Player, MutableList<Player>>()
        val playersToBlaze = HashMap<Player, MutableList<Player>>()
        val isShootIsBlackHole = HashMap<Player, Boolean>()
        val isShootIsTnt = HashMap<Player, Boolean>()
        val isShootIsTorpeda = HashMap<Player, Boolean>()
        val lastDamager = HashMap<Player, Player>()
        val arenas = mutableListOf<BlidnessArena>()
        val deathChestplate = item(Material.DIAMOND_CHESTPLATE).apply {
            meta<ItemMeta> {
                displayName = +"&c&lБронежилет M3+"
                lore = listOf(+"&6Этот нагрудик позволяет вам выбрасывать динамит после смерти. Не теряйте его.")
            }
        }

        val evilEyeChestplate = item(Material.LEATHER_CHESTPLATE).apply {
            meta<ItemMeta> {
                displayName = +"&c&lКуртка от сглаза"
                lore = listOf(+"&6Этот нагрудик позволяет вам ослеплять после смерти. Не теряйте его.")
            }

            meta<LeatherArmorMeta> {
                color = Color.fromRGB(105, 128, 124)
            }
        }

        val deathBow = item(Material.BOW).apply {
            meta<ItemMeta> {
                displayName = +"&c&lГранатомёт"
                lore = listOf(+"&8Тяжесть II", +"&6Эксплозия I")
                addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            }
        }
        val tntBullet = item(Material.TNT) { displayName = +"&7Гранаты" }

        val torpedaBullet = item(Material.BLAZE_POWDER) { displayName = +"&c&lБоеприпасы" }
        val torpedaBow = item(Material.BOW) {
            displayName = +"&3&lПушка"
            lore = listOf(+"&7Мина замедленного действия I")
            addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        }

        val plant = item(Material.BLAZE_ROD) {
            addEnchant(Enchantment.DAMAGE_ALL, 15, true)
            displayName = +"&e&lДоброта Вокруг"
        }

        val blackHole = item(Material.matchMaterial("332")) {
            displayName = +"&0&lЧёрная дыра"
            lore = listOf(+"&7Притяжение I")
        }

    }

    init {
        events {
            event<EntityDamageByEntityEvent> {
                val p = entity as? Player? ?: return@event
                val attacker = damager as? Player? ?: return@event
                if (lastDamager[p] != attacker) {
                    lastDamager[p] = attacker
                    attacker.message("&7Вас пометели...")
                }
            }


            event<PlayerInteractEvent> {
                if (item == plant) {
                    if (blazeCooldown.containsKey(player.uniqueId)) {
                        val secondsLeft =
                            (blazeCooldown[player.uniqueId]!! / 1000 + 120 * 60) - (System.currentTimeMillis() / 1000)
                        if (secondsLeft > 0) {
                            player.message("&cОсталось &6$secondsLeft &cсекунд.")
                            return@event
                        }
                    }

                    blazeCooldown[player.uniqueId] = System.currentTimeMillis()
                    player.message("&cВы успешно использовали палку Доброты")
                    val nearby = player.getNearbyEntities(7.0, 7.0, 7.0)
                    if (nearby.isNotEmpty()) {
                        nearby.forEach {
                            if (it is Player) {
                                playersToBlaze[player]?.add(it)
                                BlazeRodTask(player).runTaskTimer(plugin, 0, 20)
                                it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 300, 0))
                                it.addPotionEffect(PotionEffect(PotionEffectType.POISON, 200, 0))
                                it.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 100, 1))
                                it.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 400, 0))
                            }
                        }
                    }
                }
            }

            event<PlayerDeathEvent> {
                val p = entity

                p.inventory.chestplate ?: return@event
                if (p.inventory.chestplate.itemMeta.displayName == +"&c&lКуртка от сглаза") {
                    if (lastDamager.containsKey(p)) {
                        lastDamager[p]!!.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 120, 1))
                        lastDamager[p]!!.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20 * 120, 0))
                        lastDamager[p]!!.message("&7Вас сглазили...")
                        lastDamager.remove(p)
                    }
                }
                if (p.inventory.chestplate.itemMeta.displayName == +"&c&lБронежилет M3+") {
                    val nearby = p.getNearbyEntities(8.0, 8.0, 8.0).filterIsInstance<Player>()
                    playersToBoom[p] = nearby as MutableList<Player>
                    TntTask(p).runTaskTimer(plugin, 0, 20)
                }
            }


            event<ProjectileHitEvent> {
                val arrow = entity as? Arrow? ?: return@event
                val shooter = arrow.shooter as? Player? ?: return@event
                if (isShootIsTnt.containsKey(shooter) && isShootIsTnt[shooter]!!) {
                    arrow.world.createExplosion(arrow.location, 10.0f)
                    arrow.remove()
                }
                if (isShootIsTorpeda.containsKey(shooter) && isShootIsTorpeda[shooter]!!) {
                    val target = hitEntity as? Player? ?: return@event
                    target.fireTicks = 60
                    TorpedoTask(target).runTaskTimer(plugin, 0, 20)
                    arrow.remove()
                    isShootIsTorpeda[shooter] = false
                }
            }

            event<ProjectileHitEvent> {
                val bullet = entity as? Snowball? ?: return@event
                val shooter = bullet.shooter as? Player? ?: return@event
                if (isShootIsBlackHole.containsKey(shooter) && isShootIsBlackHole[shooter]!!) {
                    PullTask(shooter, bullet.location, 0.5, 100).runTaskTimer(plugin, 0, 1)
                    val bhHolo = HologramsAPI.createHologram(plugin, bullet.location.add(0.0, 1.5, 0.0))
                    bhHolo.appendTextLine(+"&0&lЧёрная дыра")
                    bhHolo.appendItemLine(blackHole)
                    plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                        bhHolo.delete()
                    }, 100)
                    isShootIsBlackHole[shooter] = false
                    if (snb.containsKey(shooter)) {
                        snb.remove(shooter)
                    }
                }
            }

            event<ProjectileLaunchEvent> {
                val arrow = entity as? Arrow? ?: return@event
                val shooter = arrow.shooter as? Player? ?: return@event
                val m = shooter.inventory.itemInMainHand.itemMeta ?: return@event
                if (m.displayName == +"&c&lГранатомёт") {
                    if (shooter.inventory.containsAtLeast(tntBullet, 1)) {
                        shooter.inventory.removeItem(tntBullet)
                        isShootIsTnt[shooter] = true
                    } else {
                        shooter.message("&6У вас нет динамита!")
                        isCancelled = true
                    }
                } else isShootIsTnt[shooter] = false
                if (m.displayName == +"&3&lПушка") {
                    if (shooter.inventory.containsAtLeast(torpedaBullet, 1)) {
                        shooter.inventory.removeItem(torpedaBullet)
                        isShootIsTorpeda[shooter] = true
                    }
                }
            }

            event<ProjectileLaunchEvent> {
                val bullet = entity as? Snowball? ?: return@event
                val shooter = bullet.shooter as? Player? ?: return@event
                val m = shooter.inventory.itemInMainHand.itemMeta ?: return@event
                if (m.displayName == +"&0&lЧёрная дыра") {
                    if (blackHoleCooldown.containsKey(shooter.uniqueId)) {
                        val secondsLeft =
                            (blackHoleCooldown[shooter.uniqueId]!! / 1000 + 60) - (System.currentTimeMillis() / 1000)
                        if (secondsLeft > 0) {
                            shooter.message("&cОсталось &6$secondsLeft &cсекунд.")
                            isCancelled = true
                            return@event
                        }
                    }
                    snb[shooter] = bullet
                    blackHoleCooldown[shooter.uniqueId] = System.currentTimeMillis()
                    shooter.message("&cВы успешно использовали Чёрную Дыру")
                    isShootIsBlackHole[shooter] = true
                }
            }

            event<PlayerMoveEvent> {
                if (from == to) return@event
                for (a in arenas) {
                    if (isInArena(this, a)) {
                        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 60, 0))
                    }
                }
                player.inventory.itemInMainHand.itemMeta ?: return@event
                if (player.inventory.itemInMainHand.itemMeta.displayName == +"&c&lГранатомёт") {
                    player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 30, 1))
                }
            }

            event<PlayerQuitEvent> {
                for (p in playersToBoom) {
                    if (p.value.contains(player)) {
                        p.value.remove(player)
                    }
                }
            }
        }
    }

    private fun isInArena(e: PlayerMoveEvent, a: BlidnessArena): Boolean {
        val p = e.player
        if (p.location.x > a.getXLow()!! && p.location.x < a.getXHigh()!!) {
            if (p.location.y > a.getYLow()!! && p.location.y < a.getYHigh()!!) {
                if (p.location.z > a.getZLow()!! && p.location.z < a.getZHigh()!!) {
                    return true
                }
            }
        }
        return false
    }
}
