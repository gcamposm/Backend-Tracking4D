package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Location;

public interface LocationDao extends JpaRepository<Location, Integer> {
}