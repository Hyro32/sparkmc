package dev.mardroide.lib.enums;

public enum Languages {
    ENGLISH("en"),
    SPANISH("es");

    private final String locale;

    Languages(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return this.locale;
    }
}
