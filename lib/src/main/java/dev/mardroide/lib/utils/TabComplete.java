package dev.mardroide.lib.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete {
    public static List<String> playersNamesComplete() {
        List<String> playersNames = new ArrayList<>();
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);

        for (int i = 0; i < players.length; i++) {
            playersNames.add(players[i].getName());
        }

        return playersNames;
    }
}
