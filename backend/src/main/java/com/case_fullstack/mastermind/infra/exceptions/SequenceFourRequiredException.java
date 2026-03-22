package com.case_fullstack.mastermind.infra.exceptions;

public class SequenceFourRequiredException extends RuntimeException {
    public SequenceFourRequiredException() {
        super("A sequence of 4 colors is required!");
    }
}