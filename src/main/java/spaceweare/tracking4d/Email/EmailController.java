package spaceweare.tracking4d.Email;

import spaceweare.tracking4d.Email.Desk.EmailCloseDeskReport;
import spaceweare.tracking4d.Email.Desk.Report;
import spaceweare.tracking4d.Exceptions.EmailSenderException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/email")
public class EmailController {

    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity sendEmail(){
        try {
            EmailValidator emailValidator = EmailValidator.getInstance();
            Report report = new Report();
            //jasobarzov@gmail.com
            boolean isEmailValid = emailValidator.isValidEmailAddress("guillermo.campos19@gmail.com");
            if (isEmailValid) {
                //SEND CLOSE DESK REPORT
                EmailCloseDeskReport emailCloseDeskReport = new EmailCloseDeskReport(report);
                EmailSenderHelper.sendMail("guillermo.campos19@gmail.com", emailCloseDeskReport.getVoucher(), "Cierre de caja");
                //SEND MOVE STOCK REPORT
                sendReportMoveStock("guillermo.campos19@gmail.com");

            } else {
                return ResponseEntity.badRequest().body("Invalid email address");
            }
        }catch (EmailSenderException e){
            throw new EmailSenderException("Can not send the email report, please try again", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Se ha enviado con exito");
    }
    private Boolean sendReportMoveStock(String email){
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formatDateTime = localDateTime.format(formatter);
            EmailSenderHelper.sendMail(email,
                    "texto email",
                    "Reporte de stocks disponibles en bodega " + formatDateTime);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}