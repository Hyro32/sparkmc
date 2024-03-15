package dev.mardroide.lib.jdbc.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.enums.DatabaseKeys;
import dev.mardroide.lib.enums.ModerationActions;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ModerationCollection {
    private static MongoCollection<Document> moderationCollection = Database.getCollection(Collections.MODERATION);

    public static void createBanDocument(UUID uuid, UUID agent, Date expirationDate, String reason) {
        String agentString = (agent != null) ? agent.toString() : null;
        Document document = new Document("uuid", uuid.toString());
        document.put("agent_uuid", agentString);
        document.put("action", ModerationActions.BAN);
        document.put("reason", reason);
        document.put("date", new Date());
        document.put("expiration_date", expirationDate);

        moderationCollection.insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document(DatabaseKeys.UUID.toString(), uuid.toString());
        return moderationCollection.find(document).first();
    }

    public static List<Document> findAll() {
        FindIterable<Document> documents = moderationCollection.find();
        List<Document> documentList = new ArrayList<>();

        for (Document document : documents) {
            documentList.add(document);
        }

        return documentList;
    }

    public static void delete(UUID uuid) {
        Document document = find(uuid);
        moderationCollection.findOneAndDelete(document);
    }
}
