package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityDao extends JpaRepository<Activity, Integer> {
}
