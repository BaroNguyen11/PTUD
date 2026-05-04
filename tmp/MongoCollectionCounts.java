import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoCollectionCounts {
    public static void main(String[] args) {
        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = client.getDatabase("QLNhaHang2BTCHECK");
            for (String name : new String[]{"NhanVien", "TaiKhoan", "BanAn", "MonAn", "KhachHang", "HoaDon"}) {
                System.out.println(name + "=" + db.getCollection(name).countDocuments());
            }
            Document firstNhanVien = db.getCollection("NhanVien").find().first();
            System.out.println("NhanVienFirst=" + (firstNhanVien == null ? "null" : firstNhanVien.toJson()));
        }
    }
}
