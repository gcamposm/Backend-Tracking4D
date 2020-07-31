package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Detection;
import spaceweare.tracking4d.SQL.models.Image;

import java.util.List;

public interface DetectionDao extends JpaRepository<Detection, Integer> {
    List<Detection> findAllByImage(Image image);
}
