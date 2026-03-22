package com.case_fullstack.mastermind.infra.exceptions;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException() {
        super("Match not found!");
    }
}