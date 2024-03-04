package dev.mardroide.lib.enums;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.database.Database;
import org.bson.Document;

public enum Collections {
    PLAYERS("players");

    private final String name;

    Collections(String name) {
        this.name = name;
    }

    public MongoCollection<Document> getCollection() {
        return Database.getDatabase().getCollection(this.name);
    }
}
