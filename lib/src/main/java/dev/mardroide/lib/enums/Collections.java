package dev.mardroide.lib.enums;

import com.mongodb.client.MongoCollection;
import dev.mardroide.lib.jdbc.Database;
import org.bson.Document;

public enum Collections {
    PLAYERS("players"),
    MODERATION("moderation");

    private final String name;

    Collections(String name) {
        this.name = name;
    }

    public MongoCollection<Document> getCollection() {
        return Database.getDatabase().getCollection(this.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
