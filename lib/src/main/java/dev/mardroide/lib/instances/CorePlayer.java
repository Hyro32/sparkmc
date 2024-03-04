package dev.mardroide.lib.instances;

import dev.mardroide.lib.enums.Languages;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@ToString
public class CorePlayer {
    private UUID playerId;

    @Setter
    private String language;

    private CorePlayer(UUID playerId) {
        this.playerId = playerId;
        this.language = Languages.ENGLISH.name();
    }

    public CorePlayer(Player player) {
        this.playerId = player.getUniqueId();
        this.language = player.spigot().getLocale();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }
}
