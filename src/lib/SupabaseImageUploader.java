package lib;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class SupabaseImageUploader {
    private static final String SUPABASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl4ZW14eWN5Z2toeHlnYXlkZ2NsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE2NDg5NjksImV4cCI6MjA3NzIyNDk2OX0.qQxe2vR__k8TITg5g4uaIoRwzkEBIccb2HcPm0N2sqo";
    private static final String BUCKET_NAME = "image";

    /**
     * Upload ảnh lên Supabase và trả về URL công khai (public)
     */
    public String uploadImage(File file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getName();
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

        HttpURLConnection conn = (HttpURLConnection) new URL(uploadUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_API_KEY);
        conn.setRequestProperty("Content-Type", Files.probeContentType(file.toPath()));
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            Files.copy(file.toPath(), os);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200 || responseCode == 201) {
            // ✅ Nếu bucket là Public (enable public access trong Supabase)
            return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/" + fileName;
        } else {
            throw new IOException("Upload thất bại, mã lỗi: " + responseCode);
        }
    }
}
