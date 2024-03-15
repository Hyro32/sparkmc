package dev.mardroide.lib.utils;

import dev.mardroide.lib.enums.DatabaseKeys;
import dev.mardroide.lib.enums.Languages;
import dev.mardroide.lib.enums.Reasons;
import dev.mardroide.lib.jdbc.collections.ModerationCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static List<String> defaultModerationReasons() {
        List<String> defaultReasons = new ArrayList<>();

        for (Reasons reason : Reasons.values()) {
            defaultReasons.add(reason.name());
        }

        return defaultReasons;
    }

    public static List<String> moderationPlayersNamesComplete() {
        List<String> playersNames = new ArrayList<>();

        for (Document document : ModerationCollection.findAll()) {
            UUID uuid = UUID.fromString(document.getString(DatabaseKeys.UUID.toString()));
            Player player = Bukkit.getOfflinePlayer(uuid).getPlayer();
            playersNames.add(player.getName());
        }

        return playersNames;
    }
}
