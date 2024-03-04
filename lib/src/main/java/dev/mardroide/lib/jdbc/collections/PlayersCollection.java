package dev.mardroide.lib.jdbc.collections;

import dev.mardroide.lib.enums.Collections;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PlayersCollection {
    public static void create(UUID uuid) {
        Document document = new Document("uuid", uuid);
        document.put("language", "en");
        document.put("rank", null);
        document.put("level", 0);
        document.put("experience", 0);
        document.put("first_join", new Date());
        document.put("last_join", null);

        Collections.PLAYERS.getCollection().insertOne(document);
    }

    public static Document find(UUID uuid) {
        Document document = new Document("uuid", uuid);
        return Collections.PLAYERS.getCollection().find(document).first();
    }

    public static void delete(UUID uuid) {
        Document document = new Document("uuid", uuid);
        Collections.PLAYERS.getCollection().findOneAndDelete(document);
    }
}
