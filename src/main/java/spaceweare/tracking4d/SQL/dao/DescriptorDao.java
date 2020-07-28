package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Descriptor;

public interface DescriptorDao extends JpaRepository<Descriptor, Integer> {
}
