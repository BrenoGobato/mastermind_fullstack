package com.case_fullstack.mastermind.models.dtos;

import com.case_fullstack.mastermind.models.enums.Colors;

import java.util.List;

public record AttemptRequestDTO(
        List<Colors> sequence
) {
}
