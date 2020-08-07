package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Contact;
import spaceweare.tracking4d.SQL.models.CustomerType;

public interface CustomerTypeDao extends JpaRepository<CustomerType, Integer> {
}