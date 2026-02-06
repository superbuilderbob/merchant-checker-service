package com.gomcc.merchant_checker_service.repositories;

import com.gomcc.merchant_checker_service.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
