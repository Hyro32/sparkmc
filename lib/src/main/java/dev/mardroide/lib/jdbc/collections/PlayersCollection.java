package dev.mardroide.lib.jdbc.collections;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.enums.DatabaseKeys;
import dev.mardroide.lib.enums.Languages;
import dev.mardroide.lib.enums.Ranks;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PlayersCollection {
    private static MongoCollection<Document> playersCollection = Database.getCollection(Collections.PLAYERS);

    public static void create(UUID uuid) {
        Document document = new Document();
        document.put(DatabaseKeys.UUID.toString(), uuid.toString());
        document.put(DatabaseKeys.LANGUAGE.toString(), Languages.ENGLISH);
        document.put(DatabaseKeys.RANK.toString(), Ranks.DEFAULT);
        document.put(DatabaseKeys.FIRST_JOIN.toString(), new Date());
        document.put(DatabaseKeys.LAST_JOIN.toString(), null);

        playersCollection.insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document(DatabaseKeys.UUID.toString(), uuid.toString());
        return playersCollection.find(document).first();
    }

    public static void update(UUID uuid, DatabaseKeys key, Object value) {
        Document searchQuery = new Document(DatabaseKeys.UUID.toString(), uuid.toString());
        Document updateQuery = new Document("$set", new Document(key.toString(), value));
        playersCollection.findOneAndUpdate(searchQuery, updateQuery);
    }

    public static void delete(UUID uuid) {
        Document document = new Document(DatabaseKeys.UUID.toString(), uuid);
        playersCollection.findOneAndDelete(document);
    }
}
