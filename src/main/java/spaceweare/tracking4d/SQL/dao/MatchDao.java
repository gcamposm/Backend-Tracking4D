package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchDao extends JpaRepository<Match, Integer> {
    List<Match> findMatchByHourBetween(LocalDateTime firstDate, LocalDateTime secondDate);
}
