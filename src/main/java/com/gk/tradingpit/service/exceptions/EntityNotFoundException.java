package com.gk.tradingpit.service.exceptions;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}