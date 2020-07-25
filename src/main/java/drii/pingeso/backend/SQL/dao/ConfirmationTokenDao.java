package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.Security.security.RestorePassword.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken, Integer> {
    ConfirmationToken findConfirmationTokenByToken(String token);
}