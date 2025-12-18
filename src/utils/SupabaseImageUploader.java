package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class SupabaseImageUploader {
    private static final String SUPABASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl4ZW14eWN5Z2toeHlnYXlkZ2NsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE2NDg5NjksImV4cCI6MjA3NzIyNDk2OX0.qQxe2vR__k8TITg5g4uaIoRwzkEBIccb2HcPm0N2sqo";
    private static final String BUCKET_NAME = "image";

    /**
     * Upload ảnh lên Supabase vào folder theo loại món
     * @param file File ảnh cần upload
     * @param loaiMon Tên folder (vd: "ankem", "khaivi", "monchinh")
     * @return Đường dẫn tương đối (vd: "ankem/1732467890123_banhmibotoi.jpg")
     * @throws IOException Nếu upload thất bại
     */
    public String uploadImage(File file, String loaiMon) throws IOException {
        // Tạo tên file unique với timestamp
        String fileName = System.currentTimeMillis() + "_" + file.getName();

        // Đường dẫn đầy đủ: loaiMon/fileName
        String folderPath = loaiMon + "/" + fileName;

        // URL upload
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + folderPath;

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
            return folderPath; // vd: "ankem/1732467890123_banhmibotoi.jpg"

        } else {
            // Đọc error message từ response
            String errorMsg = "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                errorMsg = br.lines().reduce("", (acc, line) -> acc + line + "\n");
            } catch (Exception e) {
                errorMsg = "Không đọc được error message";
            }

            throw new IOException("Upload thất bại, mã lỗi: " + responseCode + "\nChi tiết: " + errorMsg);
        }
    }

}