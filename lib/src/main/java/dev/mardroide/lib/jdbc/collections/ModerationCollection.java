package dev.mardroide.lib.jdbc.collections;

import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.enums.ModerationActions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ModerationCollection {
    public static void createBanDocument(UUID uuid, UUID agent, String reason) {
        Document document = new Document("uuid", uuid);
        document.put("agent_uuid", agent);
        document.put("action", ModerationActions.BAN);
        document.put("reason", reason);
        document.put("date", new Date());

        Collections.MODERATION.getCollection().insertOne(document);
    }

    public static void createWarnDocument(UUID uuid, UUID agent, String reason) {
        Document document = new Document("uuid", uuid);
        document.put("agent_uuid", agent);
        document.put("action", ModerationActions.WARN);
        document.put("warnings", new ArrayList<>());

        Collections.MODERATION.getCollection().insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document("uuid", uuid);
        return Collections.MODERATION.getCollection().find(document).first();
    }

    public static void delete(UUID uuid) {
        Document document = new Document("uuid", uuid);
        Collections.MODERATION.getCollection().findOneAndDelete(document);
    }
}
