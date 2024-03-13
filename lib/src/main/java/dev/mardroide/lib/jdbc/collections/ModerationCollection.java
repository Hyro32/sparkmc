package dev.mardroide.lib.jdbc.collections;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.enums.ModerationActions;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class ModerationCollection {
    private static MongoCollection<Document> moderationCollection = Database.getCollection(Collections.MODERATION);

    public static void createBanDocument(UUID uuid, UUID agent, String reason) {
        Document document = new Document("uuid", uuid);
        document.put("agent_uuid", agent);
        document.put("action", ModerationActions.BAN);
        document.put("reason", reason);
        document.put("date", new Date());

        moderationCollection.insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document("uuid", uuid);
        return moderationCollection.find(document).first();
    }

    public static void delete(UUID uuid) {
        Document document = new Document("uuid", uuid);
        moderationCollection.findOneAndDelete(document);
    }
}
