package com.gomcc.merchant_checker_service.repository;

import com.gomcc.merchant_checker_service.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
