package com.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private String message;
    private Object error;
}
