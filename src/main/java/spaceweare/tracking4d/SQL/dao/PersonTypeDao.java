package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.PersonType;
import java.util.Optional;

public interface PersonTypeDao extends JpaRepository<PersonType, Integer> {
    Optional<PersonType> findPersonTypeByName(String name);
}