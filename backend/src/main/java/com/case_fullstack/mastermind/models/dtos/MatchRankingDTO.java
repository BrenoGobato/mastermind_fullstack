package com.case_fullstack.mastermind.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MatchRankingDTO {
    private String username;
    private Integer totalAttempts;
    private Long durationInSeconds;
}
