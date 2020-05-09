package ru.pepej.kitpvp.utils

object TimeUtil {
    fun formatTime(secs: Int): String {
        val remainder = secs % 86400
        val days = secs / 86400
        val hours = remainder / 3600
        val minutes = remainder / 60 - hours * 60
        val seconds = remainder % 3600 - minutes * 60
        val fDays = if (days > 0) " $days дн." else ""
        val fHours = if (hours > 0) " $hours час." else ""
        val fMinutes = if (minutes > 0) " $minutes мин." else ""
        val fSeconds = if (seconds > 0) " $seconds сек." else ""
        return fDays + fHours + fMinutes + fSeconds
    }
}