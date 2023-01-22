package com.gk.tradingpit.persistance.repository;

import java.math.BigInteger;
import java.util.Optional;

import com.gk.tradingpit.persistance.entity.AffiliateClientMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AffiliateClientMapRepository extends JpaRepository<AffiliateClientMap, Long>{
	
	Optional<AffiliateClientMap> findTopByClientIdOrderByCreationDateDesc(String clientId);

}
