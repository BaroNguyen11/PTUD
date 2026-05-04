import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoTaiKhoanVerifier {
    public static void main(String[] args) {
        String uri = args.length > 0 ? args[0] : "mongodb://localhost:27017";
        String databaseName = args.length > 1 ? args[1] : "QLNhaHang2BTCHECK";
        String username = args.length > 2 ? args[2] : "admin";

        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("TaiKhoan");
            Document doc = collection.find(new Document("taiKhoan", username)).first();
            System.out.println(doc == null ? "MONGO_TAIKHOAN null" : "MONGO_TAIKHOAN " + doc.toJson());
        }
    }
}
