package ru.pepej.kitpvp.api.events.server

import org.bukkit.Server
import ru.pepej.kitpvp.api.events.KitPvPEvent

/**
 * @param type return this update type
 * Calling on server update
*/
class ServerUpdateEvent(val type: UpdateType) : KitPvPEvent()

/**
 * @param delay return delay in ticks
 */
enum class UpdateType(val delay: Long) {
    TICK(1),
    SECOND(20),
    MINUTE(1200),
    HOUR(72000)
}
