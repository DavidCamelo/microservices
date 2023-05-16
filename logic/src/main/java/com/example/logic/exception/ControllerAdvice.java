package com.example.logic.exception;

import com.example.logic.controller.DemoController;
import com.example.logic.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Slf4j
@RestControllerAdvice(assignableTypes = DemoController.class)
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
