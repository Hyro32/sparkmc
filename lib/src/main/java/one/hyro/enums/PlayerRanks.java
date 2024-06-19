package one.hyro.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public enum PlayerRanks {
    OWNER(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("OWNER", NamedTextColor.GOLD)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            7,
            List.of("hyro.ban", "hyro.kick", "hyro.tempban", "hyro.reload")
    ),
    ADMINISTRATOR(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("ADMIN", NamedTextColor.RED)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            6,
            List.of()
    ),
    MODERATOR(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("MOD", NamedTextColor.BLUE)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            5,
            List.of()
    ),
    HELPER(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("HELPER", NamedTextColor.GREEN)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            4,
            List.of()
    ),
    MVP(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("MVP", NamedTextColor.LIGHT_PURPLE)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            3,
            List.of()
    ),
    VIP(
            Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text("VIP", NamedTextColor.AQUA)
                    .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
            2,
            List.of()
    ),
    DEFAULT(null, 1, List.of());

    private final Component prefix;
    @Getter
    private final int priority;
    @Getter
    private final List<String> permissions;

    PlayerRanks(Component prefix, int priority, List<String> permissions) {
        this.prefix = prefix;
        this.priority = priority;
        this.permissions = permissions;
    }

    public Component getPrefix() {
        if (prefix == null) return Component.empty();
        return prefix.appendSpace();
    }
}
