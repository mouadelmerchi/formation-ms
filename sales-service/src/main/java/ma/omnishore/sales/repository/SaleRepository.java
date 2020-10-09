package ma.omnishore.sales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.omnishore.sales.domain.Sale;


/**
 * Spring Data  repository for the Sale entity.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByClientId(Long clientId);

    @Query("select distinct s.productCode from Sale s where s.clientId = :clientId")
    List<String> findDistinctProductCodesByClientId(@Param("clientId") Long clientId);
}
