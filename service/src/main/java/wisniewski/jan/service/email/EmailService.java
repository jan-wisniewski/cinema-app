package wisniewski.jan.service.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public final class EmailService {
    private static final String EMAIL_ADDRESS = "mailtestowykmprograms@gmail.com";
    private static final String EMAIL_PASSWORD = "";

    // https://j2html.com/
    public static void send(String to, String title, String html) {
        try {
            System.out.println("Sending email...");
            Session session = createSession();
            MimeMessage message = new MimeMessage(session);
            prepareMessage(message, to, title, html);
            Transport.send(message);
            System.out.println("Email has been sent");
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    private static void prepareMessage(MimeMessage message, String to, String title, String html) {
        try {
            message.setContent(html, "text/html;charset=utf-8");
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(title);
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    private static Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });
    }
}
