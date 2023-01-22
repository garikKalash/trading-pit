package com.gk.tradingpit.controller.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorView {
    private String message;
    private LocalDateTime time;
    private int code;
}
