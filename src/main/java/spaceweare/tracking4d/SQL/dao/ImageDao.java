package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<Image, Integer> {
}
