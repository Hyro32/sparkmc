package one.hyro.spark.lib.api.dto.connections;

import java.util.UUID;

public record ConnectionsData(
        UUID uuid,
        String discord
) {}
