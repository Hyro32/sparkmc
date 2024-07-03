package one.hyro.enums;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum MinigameTeams {
    RED("Red", NamedTextColor.RED),
    BLUE("Blue", NamedTextColor.BLUE),
    GREEN("Green", NamedTextColor.GREEN),
    YELLOW("Yellow", NamedTextColor.YELLOW),
    AQUA("Aqua", NamedTextColor.AQUA),
    PURPLE("Purple", NamedTextColor.LIGHT_PURPLE),
    GOLD("Orange", NamedTextColor.GOLD),
    WHITE("White", NamedTextColor.WHITE);

    private final String name;
    private final NamedTextColor color;

    MinigameTeams(String name, NamedTextColor color) {
        this.name = name;
        this.color = color;
    }
}
