package ru.pepej.kitpvp.configuration

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Config(private val plugin: JavaPlugin, fileName: String) :
    YamlConfiguration() {
    private val fileName: String = fileName + if (fileName.endsWith(".yml")) "" else ".yml"
    private fun createFiles() {
        try {
            val file = File(plugin.dataFolder, fileName)
            if (!file.exists()) {
                if (plugin.getResource(fileName) != null) {
                    plugin.saveResource(fileName, false)
                } else {
                    save(file)
                }
            }
            load(file)
            try {
                save(file)
            } catch (ignored: Exception) {
            }
        } catch (ignored: Exception) {
        }
    }

    fun save() {
        try {
            save(File(plugin.dataFolder, fileName))
        } catch (ignored: Exception) {
        }
    }

    init {
        createFiles()
    }
}