package one.hyro.spark.lib.api.dto.player;

import one.hyro.spark.lib.common.Rank;

import java.util.Date;
import java.util.UUID;

public record PlayerData(
    UUID uuid,
    Rank rank,
    Date rank_expiration,
    Date first_joined,
    Date last_seen
) {}
