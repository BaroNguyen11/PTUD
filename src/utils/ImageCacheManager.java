package utils;

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
            return localFile.toURI().toString();
        }

        // Chưa có → Download
        try {
            String fullUrl = supabaseUrl + relativePath;

            URL url = new URL(fullUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING);
                return localFile.toURI().toString();
            }
        } catch (Exception e) {
            System.err.println("❌ Không download được ảnh: " + relativePath);
            return null;
        }
    }

}