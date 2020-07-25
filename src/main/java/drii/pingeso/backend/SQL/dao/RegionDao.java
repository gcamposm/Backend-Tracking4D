package drii.pingeso.backend.SQL.dao;

import drii.pingeso.backend.SQL.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDao extends JpaRepository<Region, Integer> {
    Region findRegionByName(String name);
}