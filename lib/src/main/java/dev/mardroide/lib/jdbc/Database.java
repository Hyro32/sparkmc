package dev.mardroide.lib.jdbc;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.mardroide.lib.enums.Collections;
import lombok.Getter;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;

@Getter
public class Database {
    private static MongoClient client;
    private static MongoDatabase database;

    public static void connect(String uri, String databaseName) {
        if (uri == null || databaseName == null) {
            throw new IllegalArgumentException(ChatColor.RED + "Database URI and database name cannot be null.");
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .serverApi(serverApi)
                .build();

        client = MongoClients.create(settings);
        database = client.getDatabase(databaseName);

        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            database.runCommand(command);
            System.out.println("Connected to the database.");
        } catch (MongoException me) {
            System.out.println(ChatColor.RED + "Database not connected.");
        }
    }

    public static void disconnect() {
        if (client != null) {
            client.close();
            System.out.println("Disconnected from the database.");
        }
    }

    public static MongoCollection<Document> getCollection(Collections collection) {
        return database.getCollection(collection.toString());
    }
}
