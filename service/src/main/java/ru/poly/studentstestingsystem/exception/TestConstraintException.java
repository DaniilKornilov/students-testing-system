package ru.poly.studentstestingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TestConstraintException extends RuntimeException {

    public TestConstraintException(String message) {
        super(message);
    }
}
