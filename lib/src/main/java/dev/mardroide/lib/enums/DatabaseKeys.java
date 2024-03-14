package dev.mardroide.lib.enums;

import lombok.Getter;

@Getter
public enum DatabaseKeys {
    UUID("uuid"),
    LANGUAGE("language"),
    RANK("rank"),
    LEVEL("level"),
    EXPERIENCE("experience"),
    FIRST_JOIN("first_join"),
    LAST_JOIN("last_join");

    private final String key;

    DatabaseKeys(String key) {
        this.key = key;
    }
}
