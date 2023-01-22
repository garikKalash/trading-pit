package com.gk.tradingpit.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "FAILED_CALLS")
@Data
public class FailedCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String requestType;

    private String payload;

    private String reason;

    private boolean processed = false;

    private LocalDateTime time;
}
