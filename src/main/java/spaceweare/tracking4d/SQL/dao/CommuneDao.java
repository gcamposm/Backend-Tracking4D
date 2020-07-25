package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneDao extends JpaRepository<Commune, Integer> {
    Commune findCommuneByName(String name);
}