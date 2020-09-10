package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Pixel;
import spaceweare.tracking4d.SQL.models.Temperature;
import java.util.List;

public interface PixelDao extends JpaRepository<Pixel, Integer> {
    List<Pixel> findPixelByTemperatureAndX(Temperature temperature, Integer x);
    List<Pixel> findPixelByTemperature(Temperature temperature);
}