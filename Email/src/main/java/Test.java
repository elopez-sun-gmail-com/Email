
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import mx.org.mail.Mail;

/**
 *
 * @author elopez
 */
public class Test {
    
    public static void main(String[] args) throws IOException {
        
        String subject = "subject";
        String text = "Dear Mail Crawler \n\n No spam to my email, please!";
        
        List<String> emails = new ArrayList<String>();
        emails.add("elopez@ruv.org.mx");
        emails.add("ernesto_lopez_h2@hotmail.com");
        emails.add("elopez.sun@gmail.com");
        
        List<String> listFilePath = new ArrayList<String>();
        listFilePath.add("D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683.jpg");
        listFilePath.add("http://ruvcomun.blob.core.windows.net/zcudonacion/photo-of-siberian-husky-puppy-2853130-1024x6834.jpg");
        
        byte[] bytes = Files.readAllBytes(Paths.get(listFilePath.get(0)));
        
        {
            new Mail().send(emails.get(0), subject, text, listFilePath.get(0));
        }
        
        {
            new Mail().send(emails.get(0), subject, text, listFilePath);
        }
        
        {
            new Mail().send(emails, subject, text, listFilePath);
        }
        
    }
}
