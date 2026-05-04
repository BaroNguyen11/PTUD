package client.utils;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    // Thay bằng email của bạn
    private static final String SENDER_EMAIL = "bao927471@gmail.com";
    // Thay bằng 16 ký tự mật khẩu ứng dụng bạn vừa lấy (không phải mật khẩu đăng nhập)
    private static final String SENDER_APP_PASSWORD = "ohbu xsvl hqco utvq";

    public static boolean sendEmail(String toEmail, String newPassword) {
        // Cấu hình server mail của Google
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Yêu cầu cấp lại mật khẩu - Phần mềm Nhà Hàng");

            String msg = "Xin chào,\n\n"
                    + "Mật khẩu mới của bạn là: " + newPassword + "\n\n"
                    + "Vui lòng đăng nhập và đổi lại mật khẩu ngay.";

            message.setText(msg);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm sinh pass ngẫu nhiên 6 số
    public static String generateRandomPass() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
