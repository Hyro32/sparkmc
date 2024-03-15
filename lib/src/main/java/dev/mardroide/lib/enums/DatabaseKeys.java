package dev.mardroide.lib.enums;

public enum DatabaseKeys {
    UUID,
    LANGUAGE,
    RANK,
    LEVEL,
    EXPERIENCE,
    FIRST_JOIN,
    LAST_JOIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
