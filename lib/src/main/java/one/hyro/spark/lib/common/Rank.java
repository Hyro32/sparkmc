package one.hyro.spark.lib.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum Rank {
    OWNER(Component.text("OWNER")),
    ADMIN(Component.text("ADMIN")),
    MODERATOR(Component.text("MODERATOR")),
    HELPER(Component.text("HELPER")),
    SPARK_PLUS_PLUS(Component.text("SPARK++")),
    SPARK_PLUS(Component.text("SPARK+")),
    SPARK(Component.text("SPARK")),
    DEFAULT(Component.text("DEFAULT"));

    private final Component prefix;

    Rank(Component prefix) {
        this.prefix = prefix;
    }

    public Component getPrefix() {
        return Component.text("[", NamedTextColor.GRAY)
                .append(prefix)
                .append(Component.text("]", NamedTextColor.GRAY))
                .append(Component.space());
    }
}
