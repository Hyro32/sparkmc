package one.hyro.spark.lib.i18n;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    public static void setupInternationalization() {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("sparkmc:i18n"));
        ResourceBundle bundleEN = ResourceBundle.getBundle("i18n.en", Locale.ENGLISH, UTF8ResourceBundleControl.get());
        ResourceBundle bundleES = ResourceBundle.getBundle("i18n.es", Locale.forLanguageTag("es"), UTF8ResourceBundleControl.get());
        registry.registerAll(Locale.ENGLISH, bundleEN, true);
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true);
        GlobalTranslator.translator().addSource(registry);
    }
}
