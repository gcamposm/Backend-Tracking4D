package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchDao extends JpaRepository<Match, Integer> {
}
