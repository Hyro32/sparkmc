package dev.mardroide.lib.enums;

public enum Languages {
    ENGLISH("en"),
    SPANISH("es");

    private final String locale;

    Languages(String locale) {
        this.locale = locale;
    }

    public static boolean exists(String locale) {
        for (Languages language : Languages.values()) {
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
