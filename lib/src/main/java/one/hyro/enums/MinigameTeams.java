package one.hyro.enums;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum MinigameTeams {
    RED("red", NamedTextColor.RED),
    BLUE("blue", NamedTextColor.BLUE),
    GREEN("green", NamedTextColor.GREEN),
    YELLOW("yellow", NamedTextColor.YELLOW),
    AQUA("aqua", NamedTextColor.AQUA),
    PURPLE("purple", NamedTextColor.LIGHT_PURPLE);

    private final String name;
    private final NamedTextColor color;

    MinigameTeams(String name, NamedTextColor color) {
        this.name = name;
        this.color = color;
    }
}
