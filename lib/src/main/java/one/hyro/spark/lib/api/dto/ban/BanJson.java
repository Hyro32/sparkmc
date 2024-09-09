package one.hyro.spark.lib.api.dto.ban;

public record BanJson(
        String uuid,
        String agent_uuid,
        String reason,
        String created_at,
        String expires_at
) {}
