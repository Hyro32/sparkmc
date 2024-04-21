package one.hyro.lib.i18n;

import one.hyro.lib.Lib;
import one.hyro.lib.utils.Formatter;
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
}
