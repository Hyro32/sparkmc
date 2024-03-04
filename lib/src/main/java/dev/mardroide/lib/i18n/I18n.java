package dev.mardroide.lib.i18n;

import dev.mardroide.lib.enums.Languages;
import dev.mardroide.lib.utils.Colors;
import org.bukkit.entity.Player;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class I18n {
    public static String getTranslation(String locale, String key) {
        Path path = Paths.get("lib/src/main/resources/translations/" + locale + ".toml");

        try {
            TomlParseResult result = Toml.parse(path);

            if (result.hasErrors()) {
                return "Translation not found.";
            }

            return Colors.colorize(result.getString(key));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Languages defaultAvailableLocale(Player player) {
        for (Languages language : Languages.values()) {
            if (player.spigot().getLocale().startsWith(language.toString())) {
                return language;
            }
        }

        return Languages.ENGLISH;
    }
}
