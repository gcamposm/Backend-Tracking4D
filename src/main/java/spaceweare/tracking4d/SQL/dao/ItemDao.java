package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spaceweare.tracking4d.SQL.models.Item;

public interface ItemDao extends JpaRepository<Item, Integer> {
}