package one.hyro.instances;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public class GameMap {
    private final File source;
    private World activeWorld;

    public GameMap(File source) {
        this.source = source;
        load();
    }

    public void load() {
        try {
            String child = source.getName() + "_active_" + System.currentTimeMillis();
            File destDir = new File(Bukkit.getWorldContainer(), child);

            if (!destDir.exists()) {
                destDir.mkdir();
            }

            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(source));
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            World world = Bukkit.createWorld(new WorldCreator(destDir.getName()));
            if (world != null) {
                world.setAutoSave(false);
                activeWorld = world;
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to load the world: " + source.getName());
            e.printStackTrace();
        }
    }

    public void unload() {
        if (activeWorld == null) return;
        Bukkit.unloadWorld(activeWorld, false);
        File worldFolder = new File(Bukkit.getWorldContainer(), activeWorld.getName());

        for (File file : worldFolder.listFiles()) {
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    subFile.delete();
                }
                file.delete();
            }
            file.delete();
        }
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}