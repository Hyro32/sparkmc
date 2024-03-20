package dev.mardroide.lib.utils;

import dev.mardroide.lib.enums.Reasons;
import dev.mardroide.lib.i18n.Locales;
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

        for (Locales language : Locales.values()) {
            languages.add(language.name().toLowerCase());
        }

        return languages;
    }

    public static List<String> defaultModerationReasons() {
        List<String> defaultReasons = new ArrayList<>();

        for (Reasons reason : Reasons.values()) {
            defaultReasons.add(reason.name());
        }

        return defaultReasons;
    }
}
