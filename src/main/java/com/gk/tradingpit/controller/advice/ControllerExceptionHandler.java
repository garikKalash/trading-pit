package com.gk.tradingpit.controller.advice;

import com.gk.tradingpit.controller.dto.response.ErrorView;
import com.gk.tradingpit.service.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorView> handleNotFound(EntityNotFoundException ex) {
        ErrorView error = new ErrorView();
        error.setTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                     HttpStatusCode status, WebRequest request){
        ErrorView error = new ErrorView();
        error.setTime(LocalDateTime.now());
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        error.setMessage(message);
        error.setCode(status.value());
        return new ResponseEntity<>(error, status);
    }


}
