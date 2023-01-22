package com.gk.tradingpit.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "AFFILIATE_TRANSACTIONS")
@Data
public class AffiliateTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer conversionId;

    private String clientId;

    private String referralCode;

    private String orderId;

    private String currency;

    private Double orderAmount;

    private Integer conversionAmount;

    private String transactionType;

    private LocalDateTime creationDate;
}
