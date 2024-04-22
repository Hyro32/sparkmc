package one.hyro.lib.i18n;

public enum Locales {
    en_US("English (US)"),
    es_ES ("Español (España)");

    public static final Locales DEFAULT = en_US;
    private final String fullName;

    Locales(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public static boolean exists(String locale) {
        for (Locales language : Locales.values()) {
            if (language.name().equalsIgnoreCase(locale)) {
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
