package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Temperature;
import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureDao extends JpaRepository<Temperature, Integer> {
    List<Temperature> findTemperatureByDetectedHourBetween(LocalDateTime firstDate, LocalDateTime secondDate);
}