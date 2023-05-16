package com.example.auth.exception;

import com.example.auth.controller.AuthorizationController;
import com.example.auth.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Slf4j
@RestControllerAdvice(assignableTypes = AuthorizationController.class)
public class ControllerAdvice {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var errorDTO = ErrorDTO.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .error(ex)
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
