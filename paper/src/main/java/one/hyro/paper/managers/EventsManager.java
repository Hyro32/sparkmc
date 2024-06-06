package one.hyro.paper.managers;

import one.hyro.paper.events.LobbyPlayerStatusListener;
import one.hyro.paper.events.PlayerJoinListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EventsManager {
    private final List<Listener> listeners = new ArrayList<>();

    public EventsManager() {
        listeners.add(new LobbyPlayerStatusListener());
        listeners.add(new PlayerJoinListener());
    }

    public void registerEvents(Plugin plugin) {
        for (Listener listener : listeners)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
