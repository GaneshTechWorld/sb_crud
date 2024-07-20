package com.demo.M2M_Actor_Movie.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private HttpStatus code;
}