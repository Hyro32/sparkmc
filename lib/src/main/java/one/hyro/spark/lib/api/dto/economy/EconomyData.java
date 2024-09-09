package one.hyro.spark.lib.api.dto.economy;

import java.util.UUID;

public record EconomyData(
        UUID uuid,
        int purse,
        int bank
) {}
