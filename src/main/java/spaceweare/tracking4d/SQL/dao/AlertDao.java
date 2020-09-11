package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Alert;
import java.util.List;
import java.util.Optional;

public interface AlertDao extends JpaRepository<Alert, Integer> {
    Optional<Alert> findAlertById(Integer id);
    List<Alert> findAllByActive(Boolean active);
}