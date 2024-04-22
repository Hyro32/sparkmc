package one.hyro.bukkit.utils;

import one.hyro.lib.enums.Reasons;
import one.hyro.lib.i18n.Locales;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion {
    public static List<String> playersNamesComplete() {
        List<String> playersNames = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers())
            playersNames.add(player.getName());

        return playersNames;
    }

    public static List<String> availableLocalesComplete() {
        List<String> locales = new ArrayList<>();

        for (Locales locale : Locales.values())
            locales.add(locale.name());

        return locales;
    }

    public static List<String> defaultModerationReasons() {
        List<String> defaultReasons = new ArrayList<>();

        for (Reasons reason : Reasons.values())
            defaultReasons.add(reason.name().toLowerCase());

        return defaultReasons;
    }
}
