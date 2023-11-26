package com.example.order2gatherBE.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice
public class GlobalEroorHandler {


    @Getter
    @Setter
    @AllArgsConstructor
    private class ExceptionJsonResponse {
        String message;
        String errorMsg;

    }
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ExceptionJsonResponse> handleEntityNotFoundException(DataAccessException dae) {


        switch (dae.getErrorID()) {
            case 404:
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ExceptionJsonResponse(dae.getMessage(), dae.getErrorMsg()));
            case 500:
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ExceptionJsonResponse(dae.getMessage(), dae.getErrorMsg()));
            default:
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ExceptionJsonResponse(dae.getMessage(), dae.getErrorMsg()));
        }
    }
    @ExceptionHandler(ResponseEntityException.class)
    public ResponseEntity<ExceptionJsonResponse> handleResponseException(ResponseEntityException dae) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionJsonResponse(dae.getMessage(), dae.getErrorMsg()));
    }
}
