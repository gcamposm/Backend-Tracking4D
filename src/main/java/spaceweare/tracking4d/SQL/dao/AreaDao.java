package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Area;

public interface AreaDao extends JpaRepository<Area, Integer> {
}