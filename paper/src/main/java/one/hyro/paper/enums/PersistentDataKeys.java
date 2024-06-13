package one.hyro.paper.enums;

import one.hyro.paper.HyrosPaper;
import org.bukkit.NamespacedKey;

public enum PersistentDataKeys {
    CUSTOM_MENU(new NamespacedKey(HyrosPaper.getInstance(), "custom_menu"));

    private final NamespacedKey key;

    PersistentDataKeys(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
