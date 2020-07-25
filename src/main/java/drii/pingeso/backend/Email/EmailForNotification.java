package drii.pingeso.backend.Email;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.MessagingException;
import java.io.IOException;

@Data
@NoArgsConstructor
public class EmailForNotification {

    private String emailContentHtml;
    private String emailTo;
    private String subject;

    public EmailForNotification(String emailTo, String emailContentHtml, String subject){
        this.emailTo = emailTo;
        this.emailContentHtml = emailContentHtml;
        this.subject = subject;


    }
    public void sendEmail() throws IOException, MessagingException {
        EmailSenderHelper.sendMail(emailTo, this.emailContentHtml, this.subject);
    }
}