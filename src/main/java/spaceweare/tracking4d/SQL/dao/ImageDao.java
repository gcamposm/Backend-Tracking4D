package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Image;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ImageDao extends JpaRepository<Image, Integer> {
    Image findImageById(Integer id);

    List<Image> findAllByPersonAndDeleted(Person person, Boolean deleted);

    Optional<Image> findImageByPath(String path);

    Image findTopByOrderByIdDesc();

    @Query("select i.id from Image i where i.principal = true and i.person =?1")
    Integer findImageByPrincipalEquals(Person person);

    @Transactional
    @Modifying
    @Query("delete from Image i where i.id = ?1")
    void deleteImage(Integer saleDetailId);
}
