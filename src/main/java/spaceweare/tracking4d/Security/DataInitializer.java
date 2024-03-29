package spaceweare.tracking4d.Security;

import spaceweare.tracking4d.SQL.dao.UserDao;
import spaceweare.tracking4d.SQL.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Order(1)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserDao users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = users.findAll();
        if(userList.size() > 0){

            return;
        }
        else {
            //users.deleteAll();
            this.users.save(User.builder()
                    .username("userTracking4d")
                    .password(this.passwordEncoder.encode("fP8jpW477NV19FlGlYFCjT1"))
                    .roles(Arrays.asList("ROLE_USER"))
                    .build()
            );

            this.users.save(User.builder()
                    .username("adminTracking4d")
                    .password(this.passwordEncoder.encode("TZCNolIOYF2gfc0T8rejmGjH"))
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );

            this.users.save(User.builder()
                    .username("Daniel")
                    .password(this.passwordEncoder.encode("TZCNofrleYF2gfc0T8rejmGjH"))
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );

            this.users.save(User.builder()
                    .username("Jose")
                    .password(this.passwordEncoder.encode("SPCNofrleYF2gfc0w314sejmGjH"))
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );

            this.users.save(User.builder()
                    .username("Juan")
                    .password(this.passwordEncoder.encode("YTECNofrleYFw2fc0w314sejmGjH"))
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );

        }

        //log.debug("printing all users...");
        //this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}