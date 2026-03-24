package com.gomcc.merchant_checker_service.repository;

import com.gomcc.merchant_checker_service.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /*
        fuzzy lookup based on merchant_name
        @partialSearchWord:
         - `mac` -> `macdonald`
         - `bee` -> `jollibee`
         - `kentucky` -\> `kfc`
     */

    @Query(value = "SELECT mcc, name, description FROM merchant" +
            " where name ilike :partialSearchWord", nativeQuery = true)
    Merchant findMerchantDetailsByName(@Param("searchWord") String partialSearchWord);


}
