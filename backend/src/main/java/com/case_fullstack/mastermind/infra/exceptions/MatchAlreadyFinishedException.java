package com.case_fullstack.mastermind.infra.exceptions;

public class MatchAlreadyFinishedException extends RuntimeException {
    public MatchAlreadyFinishedException() {
        super("Match is already finished!");
    }
}