package com.case_fullstack.mastermind.controllers;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.case_fullstack.mastermind.infra.ControllerExceptionHandler;
import com.case_fullstack.mastermind.infra.exceptions.EmailAlreadyExistsException;
import com.case_fullstack.mastermind.infra.exceptions.InvalidPasswordException;
import com.case_fullstack.mastermind.infra.exceptions.UserNotFoundException;
import com.case_fullstack.mastermind.infra.exceptions.UsernameAlreadyExistsException;
import com.case_fullstack.mastermind.models.dtos.UserResponseDTO;
import com.case_fullstack.mastermind.services.UserService;

@WebMvcTest(UserController.class)
@Import(ControllerExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(
                1L,
                "breno",
                "breno@email.com"
        );

        when(userService.createUser(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "email": "breno@email.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("breno"))
                .andExpect(jsonPath("$.email").value("breno@email.com"));
    }

    @Test
    void shouldReturn409WhenUsernameAlreadyExists() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new UsernameAlreadyExistsException());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "email": "novo@email.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "email": "breno@email.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldLoginAndReturn200() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(
                1L,
                "breno",
                "breno@email.com"
        );

        when(userService.loginUser(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("breno"))
                .andExpect(jsonPath("$.email").value("breno@email.com"));
    }

    @Test
    void shouldReturn404WhenLoginUserNotFound() throws Exception {
        when(userService.loginUser(any()))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenPasswordIsInvalid() throws Exception {
        when(userService.loginUser(any()))
                .thenThrow(new InvalidPasswordException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "breno",
                                  "password": "senhaErrada"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}