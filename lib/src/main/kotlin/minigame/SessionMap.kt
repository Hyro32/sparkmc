package one.hyro.minigame

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.io.FileInputStream
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
        val mapName: String = zipName.substring(0, zipName.length - 4) + System.currentTimeMillis()
        val mapPath = File(Bukkit.getWorldContainer(), mapName)

        try {
            val zis = ZipInputStream(FileInputStream(zipPath))
            val entry: ZipEntry? = zis.nextEntry

            while (entry != null) {
                val file = File(mapPath, mapName)

                if (entry.isDirectory) {
                    file.mkdirs()
                } else {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                    file.outputStream().use { output ->
                        zis.copyTo(output)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val worldCreator= WorldCreator(mapName)
        return Bukkit.createWorld(worldCreator)!!
    }

    fun unload() {
        val unloaded: Boolean = Bukkit.unloadWorld(world, false)
        if (!unloaded) throw IllegalStateException("Failed to unload world.")

        val mapPath = File(Bukkit.getWorldContainer(), world.name)
        mapPath.deleteRecursively()
    }

    private fun getRandomSessionMap(): String {
        File(Bukkit.getWorldContainer(), "/maps").listFiles()?.let {
            return it.random().name
        }
        throw IllegalStateException("No maps found.")
    }
}