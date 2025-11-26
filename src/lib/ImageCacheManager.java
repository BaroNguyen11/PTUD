package lib;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class ImageCacheManager {
    private static final String CACHE_DIR = "cache/images/";

    static {
        try {
            Files.createDirectories(Paths.get(CACHE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy ảnh từ cache hoặc download về nếu chưa có
     */
    public static String getImagePath(String supabaseUrl, String relativePath) {
        String fileName = relativePath.replace("/", "_"); // ankem_banhmibotoi.jpg
        String localPath = CACHE_DIR + fileName;
        File localFile = new File(localPath);

        // Nếu đã có trong cache → dùng luôn
        if (localFile.exists()) {
            System.out.println("✅ Dùng ảnh cache: " + fileName);
            return localFile.toURI().toString();
        }

        // Chưa có → Download
        try {
            String fullUrl = supabaseUrl + relativePath;
            System.out.println("⬇️ Đang download: " + fullUrl);

            URL url = new URL(fullUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Đã cache: " + fileName);
                return localFile.toURI().toString();
            }
        } catch (Exception e) {
            System.err.println("❌ Không download được ảnh: " + relativePath);
            return null; // Trả về null để dùng ảnh mặc định
        }
    }

    /**
     * Xóa toàn bộ cache
     */
    public static void clearCache() {
        try {
            Files.walk(Paths.get(CACHE_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            System.out.println("🗑️ Đã xóa cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}