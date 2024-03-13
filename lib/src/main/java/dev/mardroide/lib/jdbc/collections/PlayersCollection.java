package dev.mardroide.lib.jdbc.collections;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PlayersCollection {
    private static MongoCollection<Document> playersCollection = Database.getCollection(Collections.PLAYERS);

    public static void create(UUID uuid) {
        Document document = new Document("uuid", uuid);
        document.put("language", "en");
        document.put("rank", null);
        document.put("level", 0);
        document.put("experience", 0);
        document.put("first_join", new Date());
        document.put("last_join", null);

        playersCollection.insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document("uuid", uuid);
        return playersCollection.find(document).first();
    }

    public static void delete(UUID uuid) {
        Document document = new Document("uuid", uuid);
        playersCollection.findOneAndDelete(document);
    }
}
