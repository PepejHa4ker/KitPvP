package ru.pepej.kitpvp.api.events.player

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import ru.pepej.kitpvp.api.events.KitPvPEvent
import ru.pepej.kitpvp.model.Kit
import ru.pepej.kitpvp.user.StatType

/**
 * abstract player event class
 * @param player return player
 * @param kit return current player kit
 *
 */

abstract class KitPvPPlayerEvent(val player: Player, val kit: Kit) : KitPvPEvent(), Cancellable {

    private var cancel: Boolean = false

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    override fun isCancelled(): Boolean {
        return cancel
    }
}

class PlayerRemoveKitEvent(
    p: Player,
    k: Kit
): KitPvPPlayerEvent(p, k)

class PlayerKilledByPlayerEvent(
    p: Player,
    k: Kit,
    val killer: Player
): KitPvPPlayerEvent(p, k)

class PlayerEditKitEvent(
    p: Player
    , k: Kit
): KitPvPPlayerEvent(p, k)

class PlayerCreateKitEvent(
    p: Player,
    k: Kit
): KitPvPPlayerEvent(p, k)

class PlayerChangeKitEvent(
    p: Player,
    k: Kit
): KitPvPPlayerEvent(p, k)

class PlayerApplyChangesEvent(
    p: Player,
    k: Kit
): KitPvPPlayerEvent(p, k)

class PlayerChangeStatisticEvent(
    val player: Player,
    val stat: StatType,
    val amount: Int
): KitPvPEvent()




