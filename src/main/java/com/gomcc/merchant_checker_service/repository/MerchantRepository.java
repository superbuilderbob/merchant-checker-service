package com.gomcc.merchant_checker_service.repository;

import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /*
        fuzzy lookup based on merchant_name
        @partialSearchWord:
         - `mac` -> `macdonald`
         - `bee` -> `jollibee`
         - `kentucky` -\> `kfc`
     */

    @Query(value = "SELECT m.id, m.name, m.mcc, m.description FROM public.merchant m where m.name ilike :name", nativeQuery = true)
    Optional<List<MerchantResponseDto>> fuzzyFindMerchantByName(@Param("name") String name);
}
