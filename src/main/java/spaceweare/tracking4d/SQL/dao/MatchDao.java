package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchDao extends JpaRepository<Match, Integer> {
}
