package server;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

public class TestChangeStream {
    public static void main(String[] args) {
        System.setProperty("app.role", "server");
        try {
            MongoDatabase db = ConnectDB.getDatabase();
            System.out.println("Testing Change Stream on MonAn...");
            var cursor = db.getCollection("MonAn").watch().iterator();
            System.out.println("Change Stream is supported!");
            cursor.close();
        } catch (Exception e) {
            System.out.println("Change Stream NOT supported: " + e.getMessage());
        } finally {
            ConnectDB.close();
        }
    }
}
