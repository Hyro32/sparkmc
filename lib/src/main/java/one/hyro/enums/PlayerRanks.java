package one.hyro.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public enum PlayerRanks {
    OWNER("OWNER", 7),
    ADMINISTRATOR("ADMIN", 6),
    MODERATOR("MOD", 5),
    HELPER("HELPER", 4),
    MVP("MVP", 3),
    VIP("VIP", 2),
    DEFAULT(null, 1);

    private final String prefix;
    private final int priority;

    PlayerRanks(String prefix, int priority) {
        this.prefix = prefix;
        this.priority = priority;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPriority() {
        return priority;
    }
}
