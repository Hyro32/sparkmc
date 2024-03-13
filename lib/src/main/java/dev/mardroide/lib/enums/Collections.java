package dev.mardroide.lib.enums;

public enum Collections {
    PLAYERS("players"),
    MODERATION("moderation");

    private final String name;

    Collections(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
