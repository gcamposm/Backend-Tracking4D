package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.PersonType;

public interface PersonTypeDao extends JpaRepository<PersonType, Integer> {
}