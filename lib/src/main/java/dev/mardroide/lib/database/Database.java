package dev.mardroide.lib.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;

public class Database {
    @Getter
    private static MongoDatabase database;
    private static final String DATABASE_NAME = "test";
    private static final String URI_URL = "test";

    public static void connect() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(URI_URL))
                .serverApi(serverApi)
                .build();

        try (MongoClient client = MongoClients.create(settings)) {
            database = client.getDatabase(DATABASE_NAME);
            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                database.runCommand(command);
                System.out.println("Connected to the database.");
            } catch (MongoException me) {
                System.err.println(me);
            }
        }
    }
}
