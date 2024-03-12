package dev.mardroide.lib.utils;

import dev.mardroide.lib.enums.Languages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete {
    public static List<String> playersNamesComplete() {
        List<String> playersNames = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playersNames.add(player.getName());
        }

        return playersNames;
    }

    public static List<String> languagesComplete() {
        List<String> languages = new ArrayList<>();

        for (Languages language : Languages.values()) {
            languages.add(language.name().toLowerCase());
        }

        return languages;
    }
}
