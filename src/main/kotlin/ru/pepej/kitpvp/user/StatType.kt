package ru.pepej.kitpvp.user

enum class StatType(val displayName: String, val formattedName: String) {
    KILLS("Kills","&cУбийства"),
    DEATHS("Deaths","&cСмерти"),
    KITS_PICKED("Kits picked","&cКитов выбрано"),
    KILLSTREAK("Killstreak", "&cКиллстрик"),
    EXPERIENCE("Experience", "&cОпыт");
}