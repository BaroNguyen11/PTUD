import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoBanAnVerifier {
    public static void main(String[] args) {
        String uri = args.length > 0 ? args[0] : "mongodb://localhost:27017";
        String databaseName = args.length > 1 ? args[1] : "QLNhaHang2BTCHECK";
        String maBan = args.length > 2 ? args[2] : "";

        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("BanAn");
            Document doc = collection.find(new Document("maBan", maBan)).first();
            System.out.println(doc == null ? "MONGO_DOC null" : "MONGO_DOC " + doc.toJson());
        }
    }
}
