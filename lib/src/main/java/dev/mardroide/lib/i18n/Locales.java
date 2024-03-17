package dev.mardroide.lib.i18n;

public enum Locales {
    ENGLISH("en"),
    SPANISH("es");

    private final String locale;

    Locales(String locale) {
        this.locale = locale;
    }

    public static boolean exists(String locale) {
        for (Locales language : Locales.values()) {
            if (language.locale.equalsIgnoreCase(locale) || language.name().equalsIgnoreCase(locale)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
