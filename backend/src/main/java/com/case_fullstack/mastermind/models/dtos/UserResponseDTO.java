package com.case_fullstack.mastermind.models.dtos;

public record UserResponseDTO(
        Long id,
        String username,
        String email
) {
}
