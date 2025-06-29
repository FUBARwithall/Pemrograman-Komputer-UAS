package db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.User;
import org.bson.Document;

public class UserDAO {
    private MongoCollection<Document> collection;

    public UserDAO() {
        MongoDatabase db = DBConnector.connect();
        collection = db.getCollection("users");
    }

    public boolean registerUser(User user) {
        if (collection.find(new Document("username", user.getUsername())).first() != null)
            return false; // user already exists

        Document doc = new Document("username", user.getUsername())
                .append("passwordHash", user.getPasswordHash());
        collection.insertOne(doc);
        return true;
    }

    public boolean validateLogin(String username, String passwordHash) {
        Document query = new Document("username", username)
                .append("passwordHash", passwordHash);
        return collection.find(query).first() != null;
    }
}
