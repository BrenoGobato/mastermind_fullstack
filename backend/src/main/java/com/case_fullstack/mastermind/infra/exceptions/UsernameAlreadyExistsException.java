package com.case_fullstack.mastermind.infra.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists!");
    }
}
