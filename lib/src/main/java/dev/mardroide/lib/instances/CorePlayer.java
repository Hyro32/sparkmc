package dev.mardroide.lib.instances;

import dev.mardroide.lib.enums.Collections;
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

    public CorePlayer(UUID playerId) {
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

    public void setLanguage(Languages language) {
        this.language = language;
        PlayersCollection.find(this.playerId).put("language", language);
    }

    public void setRank(String rank) {
        PlayersCollection.find(this.playerId).put("rank", rank);
    }

    public void setLevel(int level) {
        PlayersCollection.find(this.playerId).put("level", level);
    }

    public void setExperience(int experience) {
        PlayersCollection.find(this.playerId).put("experience", experience);
    }
}
