package one.hyro.spark.lib;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class SparkLib {
    @Getter private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        SparkLib.plugin = plugin;
        setup();
        plugin.getLogger().info("[" + plugin.getName() + "] SparkLib has been initialized!");
    }

    private static void setup() {}
}