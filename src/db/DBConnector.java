package db;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConnector {
    public static MongoDatabase connect() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        return client.getDatabase("perpustakaan");
    }
}
