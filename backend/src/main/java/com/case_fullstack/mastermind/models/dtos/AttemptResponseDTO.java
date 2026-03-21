package com.case_fullstack.mastermind.models.dtos;

import com.case_fullstack.mastermind.models.enums.Colors;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttemptResponseDTO {
    private int correctPositions;
    private MatchStatus matchStatus;
    private int attemptsLeft;
    private List<Colors> correctAnswer;

    //Constructor when match is in progress
    public AttemptResponseDTO(int correctPositions, MatchStatus matchStatus, int attemptsLeft){
        this.correctPositions = correctPositions;
        this.matchStatus = matchStatus;
        this.attemptsLeft = attemptsLeft;
    }

    //Constructor whenever user finishes a match
    public AttemptResponseDTO(int correctPositions, MatchStatus matchStatus, int attemptsLeft, List<Colors> correctAnswer){
        this.correctPositions = correctPositions;
        this.matchStatus = matchStatus;
        this.correctAnswer = correctAnswer;
        this.attemptsLeft = attemptsLeft;
    }
}


