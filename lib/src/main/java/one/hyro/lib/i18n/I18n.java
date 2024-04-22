package one.hyro.lib.i18n;

import one.hyro.lib.Lib;
import one.hyro.lib.utils.Formatter;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class I18n {
    private static final Map<String, TomlParseResult> translations = new HashMap<>();

    static {
        // Load all translations at startup
        for (Locales locale : Locales.values()) {
            loadTranslation(locale.toString());
        }
    }

    private static void loadTranslation(String locale) {
        URL url = Lib.class.getClassLoader().getResource("translations/" + locale + ".toml");
        try {
            TomlParseResult result = Toml.parse(url.openStream());
            translations.put(locale, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTranslation(String locale, String key) {
        TomlParseResult result = translations.get(locale);
        if (result == null || result.hasErrors()) return "Translation not found.";
        return Formatter.colorize(result.getString(key));
    }
}
