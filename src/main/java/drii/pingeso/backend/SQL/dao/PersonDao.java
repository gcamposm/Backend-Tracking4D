package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Integer> {
}
