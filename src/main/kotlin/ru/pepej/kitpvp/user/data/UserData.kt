package ru.pepej.kitpvp.user.data

import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.User

interface UserData {



    fun saveStatistic(user: User, stat: StatType)

    fun loadStatistics(user: User)

}