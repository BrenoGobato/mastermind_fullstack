package com.case_fullstack.mastermind.infra;

import com.case_fullstack.mastermind.infra.exceptions.*;
import com.case_fullstack.mastermind.models.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(ex.getMessage(), 409));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(ex.getMessage(), 409));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(ex.getMessage(), 404));
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleMatchNotFound(MatchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(ex.getMessage(), 404));
    }

    @ExceptionHandler(MatchAlreadyFinishedException.class)
    public ResponseEntity<ExceptionDTO> handleMatchAlreadyFinished(MatchAlreadyFinishedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(ex.getMessage(), 400));
    }

    @ExceptionHandler(SequenceFourRequiredException.class)
    public ResponseEntity<ExceptionDTO> handleSequenceFourRequired(SequenceFourRequiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(ex.getMessage(), 400));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(ex.getMessage(), 400));
    }
}
