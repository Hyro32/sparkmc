package dev.mardroide.lib.jdbc.collections;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.enums.DatabaseKeys;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PlayersCollection {
    private static MongoCollection<Document> playersCollection = Database.getCollection(Collections.PLAYERS);

    public static void create(UUID uuid) {
        Document document = new Document(DatabaseKeys.UUID.getKey(), uuid.toString());
        document.put(DatabaseKeys.LANGUAGE.getKey(), "en");
        document.put(DatabaseKeys.RANK.getKey(), null);
        document.put(DatabaseKeys.LEVEL.getKey(), 0);
        document.put(DatabaseKeys.EXPERIENCE.getKey(), 0);
        document.put(DatabaseKeys.FIRST_JOIN.getKey(), new Date());
        document.put(DatabaseKeys.LAST_JOIN.getKey(), null);

        playersCollection.insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document("uuid", uuid);
        return playersCollection.find(document).first();
    }

    public static void update(UUID uuid, DatabaseKeys key, Object value) {
        Document document = find(uuid);
        document.put(key.getKey(), value);
    }

    public static void delete(UUID uuid) {
        Document document = new Document("uuid", uuid);
        playersCollection.findOneAndDelete(document);
    }
}
