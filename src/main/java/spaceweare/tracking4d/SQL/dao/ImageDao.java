package spaceweare.tracking4d.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Image;

import javax.transaction.Transactional;

public interface ImageDao extends JpaRepository<Image, Integer> {
    Image findImageById(Integer id);

    Image findImageByName(String name);

    Image findTopByOrderByIdDesc();

    @Query("select i.id from Image i where i.principal = true and i.customer =?1")
    Integer findImageByPrincipalEquals(Customer customer);

    Image findImageByCustomerAndPrincipal(Customer customer, Boolean principal);

    @Transactional
    @Modifying
    @Query("delete from Image i where i.id = ?1")
    void deleteImage(Integer saleDetailId);
}
