package spaceweare.tracking4d.Email.Desk;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@SuppressWarnings("Duplicates")
@NoArgsConstructor
@Data
public class EmailSenderPaymentAmounth implements Runnable {

    public EmailSenderPaymentAmounth(String emailTo) {
        this.emailTo = emailTo;
    }
    private String emailTo;

    public String createReport(){
        String headers = "<html>" +
        "<!-- ######## This is a comment, visible only in the source editor  ######## -->\n" +
                "<table style=\"height: 123px; width: 637px; vertical-align: top; border-style: inset;\"  border=1 frame=void>\n" +
                "<thead>\n" +
                    "<tr style=\"height: 23px;\">\n" +
                        "<td style=\"width: 126px; height: 10px; text-align: center;\">N&uacute;mero de ticket</td>\n" +
                        "<td style=\"width: 187px; height: 10px; text-align: center;\">Total de la venta</td>\n" +
                        "<td style=\"width: 142px; height: 10px; text-align: center;\">Tipo de pago</td>\n" +
                        "<td style=\"width: 182px; height: 10px; text-align: center;\">Monto del tipo de pago</td>\n" +
                    "</tr>\n" +
                "</thead>\n";

        String body = "body";


        String finish =
                "</table>\n" +
            "<p></p>" + "</html>";
        return headers.concat(body.concat(finish));
    }

    public void sendMail() throws AddressException, MessagingException, IOException {
        //List<String> mailData = file.readEmailConfigJSON();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", "guillermo.campos19@gmail.com");
        props.setProperty("mail.smtp.password", "myPassword");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("guillermo.campos19@gmail.com","myPassword");
            }
        });
        Message msg = new MimeMessage(session);
        //msg.setFrom(new InternetAddress(mailData.get(0), false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Mail de confirmación");
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(this.createReport(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    @Override
    public void run() {
        try {
            sendMail();
        }
        catch(Exception e) {
            System.out.println("Exception caught while sending message: " + e);
        }
    }
}