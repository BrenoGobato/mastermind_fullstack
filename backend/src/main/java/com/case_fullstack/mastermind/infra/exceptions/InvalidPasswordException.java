package com.case_fullstack.mastermind.infra.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid username or password");
    }
}
