import server.ConnectDB;
import server.DevDataSeeder;

public class SeedDataNow {
    public static void main(String[] args) {
        System.setProperty("app.role", "server");
        if (args.length > 0) {
            System.setProperty("mongo.uri", args[0]);
        }
        if (args.length > 1) {
            System.setProperty("mongo.database", args[1]);
        }
        DevDataSeeder.ensureMinimumLoginData(ConnectDB.getDatabase());
        System.out.println("SEED_DONE");
    }
}
