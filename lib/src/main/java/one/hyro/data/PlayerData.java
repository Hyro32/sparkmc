package one.hyro.data;

import one.hyro.enums.PlayerRanks;
import one.hyro.utils.HyroApi;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class PlayerData {
    private final JSONObject data;

    private PlayerData(UUID uuid) {
        this.data = HyroApi.getPlayer(uuid) == null
                ? HyroApi.createPlayer(uuid)
                : HyroApi.getPlayer(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return new PlayerData(uuid);
    }

    public PlayerRanks getRank() {
        if (!data.has("rank")) return PlayerRanks.DEFAULT;
        if (getRankExpiration().before(new Date())) return PlayerRanks.DEFAULT;
        return PlayerRanks.valueOf(data.getString("rank"));
    }

    public Date getRankExpiration() {
        String expiration = data.getString("rank_expiration");
        return expiration == null ? null : new Date(expiration);
    }

    public int getLevel() {
        return data.getInt("level");
    }
}
