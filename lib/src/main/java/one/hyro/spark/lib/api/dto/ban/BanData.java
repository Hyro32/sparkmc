package one.hyro.spark.lib.api.dto.ban;

import java.util.Date;
import java.util.UUID;

public record BanData(
        UUID uuid,
        UUID agent_uuid,
        String reason,
        Date created_at,
        Date expires_at
) {}
