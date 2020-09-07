package spaceweare.tracking4d.EmailAlert;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@SuppressWarnings("Duplicates")
public class EmailAlertSender implements Runnable {

    public EmailAlertSender(String user, String mailTo, String name, String mail, String msg, String subject, String rut, String phone) {
        this.user = user;
        switch (user) {
            case "guillermo.campos19@gmail.com":
                this.password = "Scarlett141012";
                break;
            case "asesoriapersonalizada@gmail.com":
                this.password = "nuncamas2020";
                break;
            default:
                this.password = "password";
                break;
        }
        this.mailTo = mailTo;
        this.name = name;
        this.mail = mail;
        this.msg = msg;
        this.subject = subject;
        this.phone = phone;
        this.rut = rut;
    }

    private String user;
    private String password;
    private String mailTo;
    private String name;
    private String mail;
    private String msg;
    private String subject;
    private String phone;
    private String rut;

    public String createMessage() {
        String message;
        switch (user) {
            case "guillermo.campos19@gmail.com":
                message = "<h1 style=\"color: #FF0000;\">&nbsp;Alerta de temperatura</h1>\n" +
                        "<p>Datos de la persona en alerta" +
                        "<p>Nombre: " + name +
                        "</p>\n" + "<p>Rut: " + rut +
                        "</p>\n" + "<p>Correo: " + mail +
                        "</p>\n" + "<p>Celular: " + phone +
                        "</p>\n" + "<p>Comentario: " + msg + "</p>\n";
                break;
            case "asesoriapersonalizada@gmail.com":
                message = "<h1 style=\"color: #5e9ca0;\">&nbsp;Consulta</h1>\n" +
                        "<p>Nombre: " + name + "</p>\n" +
                        "<p>Correo: " + mail + "</p>\n" +
                        "<p>" + msg + "</p>\n";
                break;
            default:
                message = "<h1 style=\"color: #5e9ca0;\">&nbsp;Aquí dice consulta</h1>\n" +
                        "<p>Nombre: " + name +
                        "</p>\n" + "<p>Correo: " + mail +
                        "</p>\n" + "<p>Comentario: " + msg + "</p>\n";
                break;
        }
        return message;
    }

    public void sendMail() throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", this.user);
        props.setProperty("mail.smtp.password", this.password);
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        Message msg = new MimeMessage(session);
        //msg.setFrom(new InternetAddress("smtp.gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        msg.setSubject(this.subject);
        messageBodyPart.setContent(createMessage(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void run() {
        try {
            sendMail();
        } catch (Exception e) {
            System.out.println("Exception caught while sending message: " + e);
        }
    }
}