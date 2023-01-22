package com.gk.tradingpit.persistance.repository;

import java.math.BigInteger;

import com.gk.tradingpit.persistance.entity.FailedCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FailedCallsRepository extends JpaRepository<FailedCall, Long>{

}
