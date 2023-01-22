package com.gk.tradingpit.persistance.repository;


import com.gk.tradingpit.persistance.entity.AffiliateTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AffiliateTransactionsRepository extends JpaRepository<AffiliateTransaction, Long>{

}
