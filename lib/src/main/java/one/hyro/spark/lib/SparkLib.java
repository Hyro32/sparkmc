package one.hyro.spark.lib;

import lombok.Getter;
import one.hyro.spark.lib.interfaces.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SparkLib {
    @Getter private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        SparkLib.plugin = plugin;
        setup();
        plugin.getLogger().info("[" + plugin.getName() + "] SparkLib has been initialized!");
    }

    private static void setup() {
        if (plugin instanceof Minigame) {
            File mapsFolder = new File(Bukkit.getWorldContainer(), "maps");
            if (!mapsFolder.exists()) mapsFolder.mkdirs();
        }
    }
}