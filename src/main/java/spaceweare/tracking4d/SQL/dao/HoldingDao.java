package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Holding;
import spaceweare.tracking4d.SQL.models.Local;

public interface HoldingDao extends JpaRepository<Holding, Integer> {
}