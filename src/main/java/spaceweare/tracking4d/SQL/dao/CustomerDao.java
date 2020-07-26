package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Customer findByRut(String rut);
}
