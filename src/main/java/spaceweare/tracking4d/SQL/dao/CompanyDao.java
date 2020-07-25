package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyDao extends JpaRepository<Company, Integer> {
}
