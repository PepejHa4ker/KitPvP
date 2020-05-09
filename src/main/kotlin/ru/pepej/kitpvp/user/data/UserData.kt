package ru.pepej.kitpvp.user.data

import ru.pepej.kitpvp.user.StatType
import ru.pepej.kitpvp.user.User




interface UserData {

    /**
     * Saves player statistic into yaml or MySQL storage based on user choice
     *
     * @param user user to retrieve statistic from
     * @param stat stat to save to storage
     */
    fun saveStatistic(user: User, stat: StatType)

    /**
     * Loads player statistic from yaml or MySQL storage based on user choice
     *
     * @param user user to load statistic for
     */
    fun loadStatistics(user: User)

}