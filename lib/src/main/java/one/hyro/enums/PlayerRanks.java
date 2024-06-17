package one.hyro.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum PlayerRanks {
    OWNER(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("OWNER", NamedTextColor.GOLD)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            7
    ),
    ADMINISTRATOR(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("ADMIN", NamedTextColor.RED)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            6
    ),
    MODERATOR(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("MOD", NamedTextColor.BLUE)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            5
    ),
    HELPER(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("HELPER", NamedTextColor.GREEN)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            4
    ),
    MVP(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("MVP", NamedTextColor.LIGHT_PURPLE)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            3
    ),
    VIP(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("VIP", NamedTextColor.AQUA)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            2

    ),
    DEFAULT(null, 1);

    private final Component prefix;
    @Getter
    private final int priority;

    PlayerRanks(Component prefix, int priority) {
        this.prefix = prefix;
        this.priority = priority;
    }

    public Component getPrefix() {
        if (prefix == null) return Component.empty();
        return prefix.appendSpace();
    }
}
