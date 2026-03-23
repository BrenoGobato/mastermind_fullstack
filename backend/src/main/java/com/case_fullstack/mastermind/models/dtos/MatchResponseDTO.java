package com.case_fullstack.mastermind.models.dtos;

import com.case_fullstack.mastermind.models.enums.MatchStatus;

import java.time.LocalDateTime;

public record MatchResponseDTO(
        Long id,
        MatchStatus matchStatus,
        LocalDateTime initialDate,
        LocalDateTime finalDate
) {
}
