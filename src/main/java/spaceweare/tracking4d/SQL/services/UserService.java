package spaceweare.tracking4d.SQL.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Email.EmailForNotification;
import spaceweare.tracking4d.Email.RestorePassword;
import spaceweare.tracking4d.SQL.dao.ConfirmationTokenDao;
import spaceweare.tracking4d.SQL.dao.UserDao;
import spaceweare.tracking4d.SQL.dto.Payloads.Register.RegisterPayloadDto;
import spaceweare.tracking4d.SQL.models.*;
import spaceweare.tracking4d.Security.security.RestorePassword.ConfirmationToken;
import spaceweare.tracking4d.Security.security.RestorePassword.ConfirmationTokenProvider;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class UserService {

    private final AddressService addressService;
    final PasswordEncoder passwordEncoder;
    final ConfirmationTokenProvider confirmationTokenProvider;
    final ConfirmationTokenDao confirmationTokenDao;
    final UserDao userDao;

    public UserService(AddressService addressService, PasswordEncoder passwordEncoder, ConfirmationTokenProvider confirmationTokenProvider, ConfirmationTokenDao confirmationTokenDao, UserDao userDao) {
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenProvider = confirmationTokenProvider;
        this.confirmationTokenDao = confirmationTokenDao;
        this.userDao = userDao;
    }

    public ResponseEntity registerUser(RegisterPayloadDto registerPayloadDto){
        try{
            Commune commune = addressService.findCommuneByName(registerPayloadDto.getCommune());
            Region region = addressService.findRegionByName(registerPayloadDto.getRegion());
            Country country = new Country();
            country.setName(registerPayloadDto.getCountry());
            //Create Address
            Address address = new Address();
            address.setCountry(country);
            address.setRegion(region);
            address.setCommune(commune);
            address.setStreetName(registerPayloadDto.getStreetName());
            address.setDirectionNumber(registerPayloadDto.getDirectionNumber());
            address.setComplement(registerPayloadDto.getComplement());
            address.setCreatedAt(LocalDateTime.now());
            addressService.create(address);

            //Create User
            if(userDao.findByUsername(registerPayloadDto.getEmail()).isPresent())
            {
                return ResponseEntity.status(500).body("The username is in use");
            }
            else{
                User user = new User();
                user.setUsername(registerPayloadDto.getEmail());
                user.setActive(true);
                user.setRoles(Arrays.asList("ROLE_USER"));
                user.setCreatedAt(LocalDateTime.now());
                user.setPassword(this.passwordEncoder.encode(registerPayloadDto.getPassword()));
                userDao.save(user);
                return ResponseEntity.ok("The user has been registered successfully");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("The register process could not be completed, Error: " + e.getMessage());
        }
    }

    //AUTH AND PASSWORD
    public void forgotPassword(String userName) throws IOException, MessagingException {
        User user = this.userDao.findByUsername(userName).orElseThrow(()
                -> new UsernameNotFoundException("Username " + userName + "not found"));
        String token = confirmationTokenProvider.createToken(user);


        ConfirmationToken confirmationToken = new ConfirmationToken(LocalDateTime.now(), token, user);
        confirmationTokenDao.save(confirmationToken);

        EmailForNotification emailForNotification  = new EmailForNotification(userName,
                RestorePassword.createEmailContent(token, user.getPerson().getFirstName(), user.getPerson().getLastName()), "Recuperar contraseña");
        emailForNotification.sendEmail();
    }

    public ResponseEntity confirmResetPassword(String token){

        ConfirmationToken confirmationToken = confirmationTokenDao.findConfirmationTokenByToken(token);
        String tokenDB = confirmationToken.getToken();
        if(confirmationTokenProvider.validateConfirmationToken(tokenDB)){
            String userName = confirmationTokenProvider.getUsername(tokenDB);


            User user = this.userDao.findByUsername(userName).orElseThrow(()
                    -> new UsernameNotFoundException("Username " + userName + "not found"));
            Map<Object, Object> model = new HashMap<>();

            model.put("token", tokenDB);
            model.put("firstName", user.getPerson().getFirstName());
            model.put("lastName", user.getPerson().getLastName());
            return ok(model);
        }else{
            return ResponseEntity.status(500).body("The reset password confirmation could not be made");
        }
    }

    public void resetPassword(String token, String password){
        ConfirmationToken confirmationToken = confirmationTokenDao.findConfirmationTokenByToken(token);
        String tokenDB = confirmationToken.getToken();
        if(confirmationTokenProvider.validateConfirmationToken(tokenDB)){
            String userName = confirmationTokenProvider.getUsername(tokenDB);
            User user = this.userDao.findByUsername(userName).orElseThrow(()
                    -> new UsernameNotFoundException("Username " + userName + "not found"));
            user.setPassword(passwordEncoder.encode(password));
            userDao.save(user);
        }
    }
}
