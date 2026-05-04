import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoTaiKhoanSummary {
    public static void main(String[] args) {
        String uri = args.length > 0 ? args[0] : "mongodb://localhost:27017";
        String databaseName = args.length > 1 ? args[1] : "QLNhaHang2BTCHECK";

        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("TaiKhoan");
            System.out.println("TAIKHOAN_COUNT " + collection.countDocuments());
            for (Document doc : collection.find().limit(10)) {
                System.out.println("TAIKHOAN_DOC " + doc.toJson());
            }
        }
    }
}
