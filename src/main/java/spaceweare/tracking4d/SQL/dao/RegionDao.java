package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDao extends JpaRepository<Region, Integer> {
    Region findRegionByName(String name);
}