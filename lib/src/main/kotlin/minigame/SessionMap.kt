package one.hyro.minigame

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.io.FileInputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class SessionMap {
    var world: World

    init {
        world = load()
    }

    private fun load(): World {
        val zipName: String = getRandomSessionMap()
        val zipPath: String = Bukkit.getWorldContainer().path + "/maps/$zipName"
        val mapName: String = UUID.randomUUID().toString()
        val mapPath = File(Bukkit.getWorldContainer(), mapName)

        if (!mapPath.exists()) {
            mapPath.mkdirs()
        }

        try {
            val zis = ZipInputStream(FileInputStream(zipPath))
            var entry: ZipEntry? = zis.nextEntry

            while (entry != null) {
                val file = File(mapPath, entry.name)

                if (entry.isDirectory) {
                    file.mkdirs()
                } else {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                    file.outputStream().use { output ->
                        zis.copyTo(output)
                    }
                }
                entry = zis.nextEntry
            }

            zis.closeEntry()
            zis.close()
        } catch (e: Exception) {
            error("Failed to load map.")
        }

        val worldCreator = WorldCreator(mapName)
        return Bukkit.createWorld(worldCreator)!!
    }

    fun unload() {
        val unloaded: Boolean = Bukkit.unloadWorld(world, false)
        check(!unloaded) { "Failed to unload world." }
        val mapPath = File(Bukkit.getWorldContainer(), world.name)
        mapPath.deleteRecursively()
    }

    private fun getRandomSessionMap(): String {
        File(Bukkit.getWorldContainer(), "/maps").listFiles()?.let { return it.random().name }
        error("No maps found.")
    }
}