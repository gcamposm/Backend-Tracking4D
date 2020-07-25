package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryDao  extends JpaRepository<Country, Integer> {
}
