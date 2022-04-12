package ru.poly.studentstestingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidSignUpRequestBody extends RuntimeException {
    public InvalidSignUpRequestBody(String message) {
        super(message);
    }
}
