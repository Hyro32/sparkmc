package dev.mardroide.lib.enums;

import lombok.Getter;

@Getter
public enum Reasons {
    HACKS("moderation.reasons.hacks"),
    SPAMMING("moderation.reasons.spamming"),
    ADVERTISING("moderation.reasons.advertising"),
    INAPPROPRIATE_LANGUAGE("moderation.reasons.inappropriateLanguage"),
    INAPPROPRIATE_SKIN("moderation.reasons.inappropriateSkin"),
    INAPPROPRIATE_USERNAME("moderation.reasons.inappropriateUsername");

    private final String localeKey;

    Reasons(String localeKey) {
        this.localeKey = localeKey;
    }
}
