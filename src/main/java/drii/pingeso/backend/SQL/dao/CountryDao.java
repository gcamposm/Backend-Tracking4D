package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryDao  extends JpaRepository<Country, Integer> {
}
