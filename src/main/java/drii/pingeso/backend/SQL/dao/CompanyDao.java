package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyDao extends JpaRepository<Company, Integer> {
}
