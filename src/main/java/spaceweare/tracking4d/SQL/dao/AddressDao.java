package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressDao extends JpaRepository<Address, Integer> {

}