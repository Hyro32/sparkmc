package dev.mardroide.lib.enums;

public enum Collections {
    PLAYERS,
    MODERATION;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
