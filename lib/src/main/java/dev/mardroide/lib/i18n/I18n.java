package dev.mardroide.lib.i18n;

import dev.mardroide.lib.utils.Colors;
import net.md_5.bungee.api.ChatColor;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class I18n {
    public static String getTranslation(String locale, String key) {
        Path path = Paths.get("src/main/resources/translations/" + locale + ".toml");

        try {
            TomlParseResult result = Toml.parse(path);

            if (result.hasErrors()) {
                return Colors.colorize(getTranslation("en", key));
            }

            return Colors.colorize(result.getString(key));
        } catch (IOException e) {
            System.out.println(ChatColor.RED + "Translation not found.");
            return null;
        }
    }
}
