package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Pixel;

public interface PixelDao extends JpaRepository<Pixel, Integer> {
}