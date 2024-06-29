package one.hyro.instances;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public class GameMap {
    private final String zipName;
    private final World world;
    private final Plugin plugin;

    public GameMap(String zipName, Plugin plugin) {
        this.zipName = zipName;
        this.plugin = plugin;
        this.world = load();
    }


    public World load() {
        String worldName = zipName.substring(0, zipName.length() - 4) + "_active_" + System.currentTimeMillis();
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        String pathToZip = plugin.getDataFolder().getAbsolutePath() + File.separator + "maps" + File.separator + zipName;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(worldFolder, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldCreator creator = new WorldCreator(worldName);
        return Bukkit.createWorld(creator);
    }

    public void unload() {
        if (Bukkit.unloadWorld(world, false)) {
            deleteDirectory(world.getWorldFolder());
        }
    }

    private void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}