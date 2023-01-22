package com.gk.tradingpit.service.exercise_tap.exception;

import com.gk.tradingpit.service.exercise_tap.model.request.ExerciseTapConversion;
import lombok.Data;

@Data
public class ApiConversationExternalException extends RuntimeException {
    private ExerciseTapConversion exerciseTapConversion;
    public ApiConversationExternalException(String message) {
        super(message);
    }
}
