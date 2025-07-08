package db;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConnector {
    public static MongoDatabase connect() {
        MongoClient client = MongoClients.create("mongodb+srv://panjirafi96:oK6Nft0tWAmbL03e@perpustakaan.zbxakzu.mongodb.net/?retryWrites=true&w=majority&appName=perpustakaan");
        return client.getDatabase("perpustakaan");
    }
}
