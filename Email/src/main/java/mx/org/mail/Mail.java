package mx.org.mail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author elopez
 */
public class Mail {

    private String username = "respuestas@ruv.org.mx";
    private String password = "Jawu7380";

    private final Properties properties = new Properties();
    private Session session;

    public Mail() {
        this.init();
    }

    private void init() {

        this.properties.put("mail.smtp.host", "smtp.office365.com");
        this.properties.put("mail.smtp.port", 587);
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.socketFactory.port", 587);
        this.properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        this.properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        this.session = Session.getInstance(this.properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     *
     * @param to
     * @param subject
     * @param text
     * @param filePath
     * @throws URISyntaxException
     */
    public void send(String to, String subject, String text, String filePath) throws IOException {

        List<String> listTo = new ArrayList<String>();

        listTo.add(to);

        List<String> listFilePath = new ArrayList<String>();

        listFilePath.add(filePath);

        this.send(listTo, subject, text, listFilePath);

    }

    /**
     *
     * @param to
     * @param subject
     * @param text
     * @param listFilePaths
     * @throws URISyntaxException
     */
    public void send(String to, String subject, String text, List<String> listFilePaths) throws IOException {

        List<String> listTo = new ArrayList<String>();

        listTo.add(to);

        this.send(listTo, subject, text, listFilePaths);

    }

    /**
     *
     * @param to
     * @param subject
     * @param text
     * @param listFilePaths
     */
    public void send(List<String> to, String subject, String text, List<String> listFilePaths) throws IOException {

        try {

            if (to != null && to.size() > 0) {

                for (String item : to) {

                    Message message = new MimeMessage(this.getSession());
                    message.setFrom(new InternetAddress(this.username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(item));
                    message.setSubject(subject);
                    message.setText(text);

                    if (listFilePaths != null && listFilePaths.size() > 0) {

                        // Create a multipar message     
                        Multipart multipart = new MimeMultipart();

                        int cont = 1;

                        for (String filePath : listFilePaths) {

                            FileDataSource file = new FileDataSource(filePath);

                            if (file != null) {

                                System.out.println("File --> " + file.getName());
                                
                                // Create the message part
                                BodyPart messageBodyPart = new MimeBodyPart();

                                // Fill the message
                                messageBodyPart.setText(text);

                                // Create a multipar message
                                //Multipart multipart = new MimeMultipart();
                                // Set text message part
                                if (cont == 1) {
                                    multipart.addBodyPart(messageBodyPart);
                                }

                                // Part two is attachment
                                messageBodyPart = new MimeBodyPart();

                                if (filePath.contains("https://") || filePath.contains("http://")) {
    
                                    URL domain = new URL(filePath);
                                            
                                    messageBodyPart.setDataHandler(new DataHandler(domain));                                 
                                    
                                } else {
                                    
                                     DataSource source = new FileDataSource(filePath);
                                    
                                     messageBodyPart.setDataHandler(new DataHandler(source));
                                }

                                //messageBodyPart.setDataHandler(new DataHandler(source));
                                messageBodyPart.setFileName(file.getName());
                                multipart.addBodyPart(messageBodyPart);

                                // Send the complete message parts
                            }

                            cont++;
                        }

                        message.setContent(multipart);

                    }

                    Transport.send(message);

                    System.out.println("Done");

                }

            }

        } catch (MessagingException e) {
            System.out.println("MessagingException -->> " + e.getMessage());
        }

    }

}
