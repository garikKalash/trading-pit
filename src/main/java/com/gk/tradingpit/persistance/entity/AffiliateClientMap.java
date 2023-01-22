package com.gk.tradingpit.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "AFFILIATE_CLIENT_MAP")
@Data
public class AffiliateClientMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String referralCode;

    private String clickId;

    private String userAgent;

    private String ip;

    private LocalDateTime creationDate;

}
