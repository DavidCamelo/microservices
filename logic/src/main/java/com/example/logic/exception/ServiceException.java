package com.example.logic.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
        log.error(message);
    }
}
