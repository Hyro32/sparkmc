package one.hyro.spark.lib.session;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class SessionMap {
    public SessionMap() {
        load();
    }

    public World load() {
        WorldCreator creator = new WorldCreator("test");
        return Bukkit.createWorld(creator);
    }

    public void unload() {
        // Unload the world
    }
}
