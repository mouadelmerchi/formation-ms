package ma.omnishore.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.omnishore.products.domain.Product;


/**
 * Spring Data  repository for the Sale entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select distinct p.supplier from Product p where p.code in :codes")
    List<String> findProductsByCodeList(@Param("codes") List<String> codes);

}
