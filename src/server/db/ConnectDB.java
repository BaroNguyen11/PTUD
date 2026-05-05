package server.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class ConnectDB {
    private static final String DEFAULT_URI = "mongodb+srv://bao927471_db_user:2oyOwLxpNs5mfiqL@cluster0.e2esjex.mongodb.net/?appName=Cluster0";
    private static final String DEFAULT_DATABASE = "QLNhaHang2BTCHECK";

    private static MongoClient client;
    private static MongoDatabase database;

    private ConnectDB() {
    }

    public static synchronized MongoDatabase getDatabase() {
        ensureServerRole();
        if (database == null) {
            String uri = System.getProperty("mongo.uri", DEFAULT_URI);
            String dbName = System.getProperty("mongo.database", DEFAULT_DATABASE);
            client = MongoClients.create(uri);
            database = client.getDatabase(dbName);
        }
        return database;
    }

    public static synchronized void close() {
        ensureServerRole();
        if (client != null) {
            client.close();
            client = null;
            database = null;
        }
    }

    private static void ensureServerRole() {
        if (!"server".equals(System.getProperty("app.role"))) {
            throw new IllegalStateException("MongoDB access is server-only. Start through server.config.ServerMain.");
        }
    }

    public static void main(String[] args) {
        System.setProperty("app.role", "server");
        getDatabase().listCollectionNames().first();
        System.out.println("MongoDB connected");
    }
}
