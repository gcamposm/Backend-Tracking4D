package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDao  extends JpaRepository<City, Integer> {
}
