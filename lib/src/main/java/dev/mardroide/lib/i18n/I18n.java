package dev.mardroide.lib.i18n;

import dev.mardroide.lib.Lib;
import dev.mardroide.lib.utils.Formatter;
import org.bukkit.entity.Player;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.net.URL;

public class I18n {
    public static String getTranslation(String locale, String key) {
        URL url = Lib.class.getClassLoader().getResource("translations/" + locale + ".toml");

        try {
            TomlParseResult result = Toml.parse(url.openStream());
            if (result.hasErrors()) return "Translation not found.";
            return Formatter.colorize(result.getString(key));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Locales defaultAvailableLocale(Player player) {
        for (Locales language : Locales.values()) {
            if (player.spigot().getLocale().startsWith(language.toString())) {
                return language;
            }
        }

        return Locales.ENGLISH;
    }
}
