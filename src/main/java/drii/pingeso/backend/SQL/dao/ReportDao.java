package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDao extends JpaRepository<Report, Integer> {
}
