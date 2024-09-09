package one.hyro.spark.lib.api.dto.player;

public record PlayerJson(
        String uuid,
        String rank,
        String rank_expiration,
        String first_joined,
        String last_seen
) {}
