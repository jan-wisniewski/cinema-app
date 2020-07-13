package wisniewski.jan.service.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class EmailService {

    private static final String EMAIL_FROM = "no-reply@greencinema.com";
    private static final String HOST = "localhost";
    private static final String SMTP = "mail.smtp.host";
    private static Properties properties;

    public static void sendMail(String to, String title, String text) {
        properties = System.getProperties();
        properties.setProperty(SMTP,HOST);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}