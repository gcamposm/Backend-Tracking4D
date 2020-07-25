package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityDao extends JpaRepository<Activity, Integer> {
}
