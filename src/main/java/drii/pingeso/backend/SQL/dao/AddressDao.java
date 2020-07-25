package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressDao extends JpaRepository<Address, Integer> {

}