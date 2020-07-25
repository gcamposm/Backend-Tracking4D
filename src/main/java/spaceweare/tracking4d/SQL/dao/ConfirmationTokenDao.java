package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.Security.security.RestorePassword.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken, Integer> {
    ConfirmationToken findConfirmationTokenByToken(String token);
}