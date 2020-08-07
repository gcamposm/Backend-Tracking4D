package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonDao extends JpaRepository<Person, Integer> {
    Person findByRut(String rut);
    Optional<Person> findCustomerByFirstNameAndLastName(String firstName, String lastName);
    Optional<Person> findCustomerByRut(String rut);
    Optional<Person> findCustomerByFirstName(String firstName);
    List<Person> findAllByUnknown(Boolean unknown);
    List<Person> findAllByUnknownAndDeleted(Boolean unknown, Boolean deleted);
}