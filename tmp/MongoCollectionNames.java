import com.mongodb.client.*;
public class MongoCollectionNames {
  public static void main(String[] args) {
    try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
      MongoDatabase db = client.getDatabase("QLNhaHang2BTCHECK");
      for (String name : db.listCollectionNames()) System.out.println(name);
    }
  }
}
