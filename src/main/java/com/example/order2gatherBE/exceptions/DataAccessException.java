package com.example.order2gatherBE.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;
    private String errorMsg;
    private int errorID; //Not Found: 404, Internal Error: 500

    public DataAccessException(int errorID, String message, String errorMsg) {
        super();
        System.out.println(message);
        this.errorID = errorID;
        this.message = message;
        this.errorMsg = errorMsg;
    }
}
