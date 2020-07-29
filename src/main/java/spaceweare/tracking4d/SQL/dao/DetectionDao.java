package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Detection;

public interface DetectionDao extends JpaRepository<Detection, Integer> {
}
