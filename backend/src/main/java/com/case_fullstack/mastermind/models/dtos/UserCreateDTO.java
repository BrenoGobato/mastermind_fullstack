package com.case_fullstack.mastermind.models.dtos;

public record UserCreateDTO(
   String username,
   String email,
   String password
) {}
