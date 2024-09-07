package one.hyro.spark.smp;

import org.bukkit.plugin.java.JavaPlugin;

public class SparkSmp extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("SparkSmp has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkSmp has been disabled!");
    }
}