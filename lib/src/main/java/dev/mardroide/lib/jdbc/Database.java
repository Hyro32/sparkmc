package dev.mardroide.lib.jdbc;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;

public class Database {
    @Getter
    private static MongoDatabase database;

    public static void connect(String uri, String databaseName) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();

        try (MongoClient client = MongoClients.create(settings)) {
            database = client.getDatabase(databaseName);
            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                database.runCommand(command);
                System.out.println("Connected to the database.");
            } catch (MongoException me) {
                System.out.println(ChatColor.RED + "Database not connected.");
            }
        }
    }
}
