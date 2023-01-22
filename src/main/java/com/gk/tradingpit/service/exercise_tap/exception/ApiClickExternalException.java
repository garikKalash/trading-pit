package com.gk.tradingpit.service.exercise_tap.exception;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import lombok.Data;

@Data
public class ApiClickExternalException extends RuntimeException {
    private ClientClickDto clientClickDto;
    public ApiClickExternalException(String message) {
        super(message);
    }
}