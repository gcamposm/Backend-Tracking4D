package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Customer findByRut(String rut);
    Optional<Customer> findCustomerByFirstNameAndLastName(String firstName, String lastName);
    Optional<Customer> findCustomerByRut(String rut);
    Optional<Customer> findCustomerByFirstName(String firstName);
    List<Customer> findCustomerByHourBetween(LocalDateTime firstDate, LocalDateTime secondDate);
}