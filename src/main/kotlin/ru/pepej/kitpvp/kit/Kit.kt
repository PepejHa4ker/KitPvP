package ru.pepej.kitpvp.kit

import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import java.util.*


data class Kit(val kitName: String, val cost: Double, val item: ArrayList<ItemStack>, val effects: ArrayList<PotionEffect>, val formattedName: String, val delay: Long)











