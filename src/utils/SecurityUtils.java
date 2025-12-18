package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    // Hàm này nhận vào mật khẩu thường và trả về chuỗi mã hóa SHA-256
    public static String encrypt(String password) {
        try {
            // Chọn thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Băm chuỗi password
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Chuyển đổi byte array sang Hex String (chuỗi ký tự đọc được)
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Hoặc ném ngoại lệ
        }
    }
}