package ru.pepej.kitpvp.user

import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.onlinePlayers
import org.bukkit.entity.Player
import ru.pepej.kitpvp.user.data.FileStats
import ru.pepej.kitpvp.user.data.UserData
import java.util.*


object UserManager {
    private val users = ArrayList<User>()
    private var data: UserData? = null

    init {
        data = FileStats
        loadStatsForPlayersOnline()
    }

    private fun loadStatsForPlayersOnline() {
        for (player in onlinePlayers()) {
            val user = getUser(player)
            loadStatistics(user)
        }
    }


    fun getUser(player: Player): User {
        for (user in users) {
            if (user.player == player) {
                return user
            }
        }
        val user = User(player)
        users.add(user)
        return user
    }

    fun getUsers(): List<User?>? {
        val users: MutableList<User?> =
            ArrayList()
        for (player in onlinePlayers()) {
            users.add(getUser(player!!))
        }
        return users
    }

    fun saveStatistic(user: User, stat: StatType) {
        data!!.saveStatistic(user, stat)
    }

    fun loadStatistics(user: User) {
        data!!.loadStatistics(user)

    }

    fun removeUser(user: User?) {
        users.remove(user)
    }
}
