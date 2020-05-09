package ru.pepej.kitpvp

import org.bukkit.Location
import org.bukkit.World

class BlidnessArena(w: World, x: Double, y: Double, z: Double, radius: Double) : Location(w, x, y, z) {

    private var xHigh: Double? = null
    private var xLow: Double? = null
    private var yHigh: Double? = null
    private var yLow: Double? = null
    private var zHigh: Double? = null
    private var zLow: Double? = null


    init {
        xHigh = getX() + radius
        xLow = getX() - radius
        yHigh = getY() + radius
        yLow = getY() - radius
        zHigh = getZ() + radius
        zLow = getZ() - radius
    }

    fun getXHigh() : Double? {
        return xHigh
    }

    fun getXLow() : Double? {
        return xLow
    }

    fun getYHigh() : Double? {
        return yHigh
    }

    fun getYLow() : Double? {
        return yLow
    }

    fun getZHigh() : Double? {
        return zHigh
    }

    fun getZLow() : Double? {
        return zLow
    }


}