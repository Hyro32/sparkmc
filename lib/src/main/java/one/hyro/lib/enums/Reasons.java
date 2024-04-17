package one.hyro.lib.enums;

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

    public String getLocaleKey() {
        return localeKey;
    }
}
