package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Local;

public interface LocalDao extends JpaRepository<Local, Integer> {
}