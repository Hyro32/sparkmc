package dev.mardroide.lib.instances;

import dev.mardroide.lib.enums.Languages;
import dev.mardroide.lib.i18n.I18n;
import dev.mardroide.lib.jdbc.collections.PlayersCollection;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@ToString
public class CorePlayer {
    private UUID playerId;
    private Languages language;

    private CorePlayer(UUID playerId) {
        this.playerId = playerId;
        this.language = Languages.ENGLISH;
        PlayersCollection.create(this.playerId);
    }

    public CorePlayer(Player player) {
        this.playerId = player.getUniqueId();
        this.language = I18n.defaultAvailableLocale(player);
        PlayersCollection.create(this.playerId);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }
}
