package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDao  extends JpaRepository<City, Integer> {
}
