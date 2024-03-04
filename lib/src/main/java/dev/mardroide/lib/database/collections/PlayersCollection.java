package dev.mardroide.lib.database.collections;

import dev.mardroide.lib.enums.Collections;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PlayersCollection {
    public static void create(UUID uuid) {
        Document document = new Document("uuid", uuid);
        document.put("rank", null);
        document.put("level", 0);
        document.put("experience", 0);
        document.put("first_join", new Date());
        document.put("last_join", null);

        Collections.PLAYERS.getCollection().insertOne(document);
    }

    public static Document find(UUID uuid) {
        return Collections.PLAYERS.getCollection().find(new Document("uuid", uuid)).first();
    }

    public static void update() {
        // Update a document here
    }

    public static void delete() {
        // Delete a document here
    }
}
