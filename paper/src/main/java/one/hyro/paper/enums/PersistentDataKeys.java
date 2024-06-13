package one.hyro.paper.enums;

public enum PersistentDataKeys {
    CUSTOM_MENU("custom-menu");

    private final String key;

    PersistentDataKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
