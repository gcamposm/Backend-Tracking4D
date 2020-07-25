package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Integer> {
}
