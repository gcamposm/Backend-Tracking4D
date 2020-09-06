package spaceweare.tracking4d.EmailAlert;

import spaceweare.tracking4d.Exceptions.EmailSenderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mailAlert")
public class EmailAlertController {

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity sendMail(@RequestParam("user") String user,
                                   @RequestParam("name") String name,
                                   @RequestParam("mail") String mail,
                                   @RequestParam("rut") String rut,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("mailTo") String mailTo,
                                   @RequestParam("msg") String msg,
                                   @RequestParam("subject") String subject) {
        try {
            EmailAlertValidator mailValidator = EmailAlertValidator.getInstance();
            if (mailValidator.isValidEmailAddress(user)) {
                Runnable emailSender = new EmailAlertSender(user, mailTo, name, mail, msg, subject, rut, phone);
                emailSender.run();
                return ResponseEntity.status(HttpStatus.OK).body("Mensaje enviado con exito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La dirección de correo ingresada no es válida.");
            }
        } catch (EmailSenderException e) {
            throw new EmailSenderException("Can not send the email, please try again", e);
        }
    }

    private String createMscMessage(String phone, String commune, String message) {
        message = "<p>Contacto: " + phone + "</p>\n" +
                "<p>Comuna: " + commune + "</p>\n" +
                "<p>Comentario: " + message + "</p>\n";
        return message;
    }
}