package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDao extends JpaRepository<Report, Integer> {
}
