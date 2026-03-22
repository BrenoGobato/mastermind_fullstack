package com.case_fullstack.mastermind.controllers;

import com.case_fullstack.mastermind.models.dtos.UserCreateDTO;
import com.case_fullstack.mastermind.models.dtos.UserLoginDTO;
import com.case_fullstack.mastermind.models.dtos.UserResponseDTO;
import com.case_fullstack.mastermind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO response = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserResponseDTO response = userService.loginUser(userLoginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
