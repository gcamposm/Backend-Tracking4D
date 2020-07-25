package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneDao extends JpaRepository<Commune, Integer> {
    Commune findCommuneByName(String name);
}