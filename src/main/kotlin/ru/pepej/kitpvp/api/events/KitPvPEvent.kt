package ru.pepej.kitpvp.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Abstract event class
 */

abstract class KitPvPEvent : Event() {
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }
}