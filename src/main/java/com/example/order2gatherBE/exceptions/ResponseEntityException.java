package com.example.order2gatherBE.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseEntityException extends RuntimeException {
    private static final long serialVersionUID = 3156815846745801694L;
    private String message;
    private String errorMsg;
}
