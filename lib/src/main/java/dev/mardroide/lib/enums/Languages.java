package dev.mardroide.lib.enums;

import lombok.Getter;

@Getter
public enum Languages {
    ENGLISH("en"),
    SPANISH("es");

    private final String locale;

    Languages(String locale) {
        this.locale = locale;
    }
}
