package one.hyro.spark.lib;

import lombok.Getter;
import one.hyro.spark.lib.common.listener.SparkInventoryListener;
import one.hyro.spark.lib.common.listener.SparkItemInteractListener;
import one.hyro.spark.lib.interfaces.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SparkLib {
    @Getter private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        SparkLib.plugin = plugin;
        setup();
    }

    private static void setup() {
        if (plugin instanceof Minigame) {
            File mapsFolder = new File(Bukkit.getWorldContainer(), "maps");
            if (!mapsFolder.exists()) mapsFolder.mkdirs();
        }

        plugin.getServer().getPluginManager().registerEvents(new SparkInventoryListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SparkItemInteractListener(), plugin);
    }
}