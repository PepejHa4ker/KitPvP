@file:Suppress("DEPRECATION")

plugins {
    kotlin("jvm") version "1.3.71"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("java")
}

group = "ru.pepej"
version = "1.0.0"

val spigotVersion = "1.12.2-R0.1-SNAPSHOT"
val kotlinApiVersion = "0.1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("http://nexus.devsrsouza.com.br/repository/maven-public/")}
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")}
}

dependencies {
    compileOnly(fileTree("C:\\Users\\Глад Валакас\\Desktop\\Server\\lobby\\plugins\\HolographicDisplays.jar"))
    compileOnly(fileTree("C:\\Users\\Глад Валакас\\Desktop\\Server\\lobby\\plugins\\TouchscreenHolograms.jar"))
    compileOnly(fileTree("C:\\Users\\Глад Валакас\\Desktop\\Server\\lobby\\plugins\\CustomHeads.jar"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.12-SNAPSHOT")
    compileOnly("br.com.devsrsouza.kotlinbukkitapi:core:$kotlinApiVersion")
    compileOnly("br.com.devsrsouza.kotlinbukkitapi:architecture:$kotlinApiVersion")
    compileOnly("br.com.devsrsouza.kotlinbukkitapi:exposed:$kotlinApiVersion")
    compileOnly("br.com.devsrsouza.kotlinbukkitapi:plugins:$kotlinApiVersion")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.10.5")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    shadowJar {
        destinationDir = file("C:\\Users\\Глад Валакас\\Desktop\\Server\\lobby\\plugins")
        classifier = null
    }
}

