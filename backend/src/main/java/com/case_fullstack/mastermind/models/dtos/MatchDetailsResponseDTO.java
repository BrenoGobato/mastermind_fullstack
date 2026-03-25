package com.case_fullstack.mastermind.models.dtos;

import com.case_fullstack.mastermind.models.enums.Colors;
import com.case_fullstack.mastermind.models.enums.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MatchDetailsResponseDTO(
        Long id,
        MatchStatus matchStatus,
        LocalDateTime initialDate,
        LocalDateTime finalDate,
        List<AttemptDetailsDTO> attempts,
        List<Colors> correctAnswer
) {
}