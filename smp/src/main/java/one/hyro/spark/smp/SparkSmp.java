package one.hyro.spark.smp;

import lombok.Getter;
import one.hyro.spark.lib.i18n.I18n;
import org.bukkit.plugin.java.JavaPlugin;

public class SparkSmp extends JavaPlugin {
    @Getter private static SparkSmp instance;

    @Override
    public void onEnable() {
        instance = this;
        I18n.setupInternationalization();
        getLogger().info("SparkSmp has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkSmp has been disabled!");
    }
}