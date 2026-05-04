import client.utils.SecurityUtils;
import common.DangNhapRemote;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.rmi.Naming;

public class LoginDiagnosticsProbe {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String username = args.length > 2 ? args[2] : "admin";
        String plainPassword = args.length > 3 ? args[3] : "123456";
        String mongoUri = args.length > 4 ? args[4] : "mongodb://localhost:27017";
        String mongoDb = args.length > 5 ? args[5] : "QLNhaHang2BTCHECK";

        DangNhapRemote remote = (DangNhapRemote) Naming.lookup(
                "rmi://" + host + ":" + port + "/DangNhapRemote");

        String hashedPassword = SecurityUtils.encrypt(plainPassword);

        System.out.println("USERNAME_EXISTS " + remote.isUsernameExist(username));
        System.out.println("AUTH_PLAIN " + remote.authenticate(username, plainPassword));
        System.out.println("AUTH_HASH " + remote.authenticate(username, hashedPassword));
        System.out.println("IS_ACTIVE " + remote.isTaiKhoanHoatDong(username));
        System.out.println("MA_NV " + remote.getMaNhanVien(username));

        try (MongoClient client = MongoClients.create(mongoUri)) {
            MongoDatabase database = client.getDatabase(mongoDb);
            MongoCollection<Document> collection = database.getCollection("TaiKhoan");
            Document doc = collection.find(new Document("taiKhoan", username)).first();
            System.out.println(doc == null ? "MONGO_TAIKHOAN null" : "MONGO_TAIKHOAN " + doc.toJson());
        }
    }
}
