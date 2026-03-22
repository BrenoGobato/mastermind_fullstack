package com.case_fullstack.mastermind.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionDTO {

    private String message;
    private String statusCode;
}
