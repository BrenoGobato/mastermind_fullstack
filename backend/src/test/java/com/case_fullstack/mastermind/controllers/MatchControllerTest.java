package com.case_fullstack.mastermind.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.case_fullstack.mastermind.infra.ControllerExceptionHandler;
import com.case_fullstack.mastermind.infra.exceptions.MatchAlreadyFinishedException;
import com.case_fullstack.mastermind.infra.exceptions.MatchNotFoundException;
import com.case_fullstack.mastermind.infra.exceptions.SequenceFourRequiredException;
import com.case_fullstack.mastermind.models.dtos.AttemptResponseDTO;
import com.case_fullstack.mastermind.models.dtos.MatchDetailsResponseDTO;
import com.case_fullstack.mastermind.models.dtos.MatchResponseDTO;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.services.MatchService;

@WebMvcTest(MatchController.class)
@Import(ControllerExceptionHandler.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    @Test
    void shouldStartMatch() throws Exception {
        MatchResponseDTO response = new MatchResponseDTO(
                1L,
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now(),
                null
        );

        when(matchService.startMatch(any())).thenReturn(response);

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.matchStatus").value("IN_PROGRESS"));
    }

    @Test
    void shouldGetMatchById() throws Exception {

        MatchDetailsResponseDTO response = new MatchDetailsResponseDTO(
                1L,
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now(),
                null,
                List.of(), // attempts
                null       // correctAnswer (ou o que seu DTO tiver)
        );

        when(matchService.findMatchById(1L)).thenReturn(response);

        mockMvc.perform(get("/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.matchStatus").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn404WhenMatchNotFound() throws Exception {
        when(matchService.findMatchById(1L))
                .thenThrow(new MatchNotFoundException());

        mockMvc.perform(get("/matches/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldMakeAttempt() throws Exception {
        AttemptResponseDTO response = new AttemptResponseDTO(
                2,
                MatchStatus.IN_PROGRESS,
                9,
                null
        );

        when(matchService.makeAttempt(any(), any())).thenReturn(response);

        mockMvc.perform(post("/matches/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sequence": ["RED","BLUE","YELLOW","WHITE"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correctPositions").value(2))
                .andExpect(jsonPath("$.matchStatus").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn404WhenMatchNotFoundOnAttempt() throws Exception {
        when(matchService.makeAttempt(any(), any()))
                .thenThrow(new MatchNotFoundException());

        mockMvc.perform(post("/matches/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sequence": ["RED","BLUE","YELLOW","WHITE"]
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenSequenceInvalid() throws Exception {
        when(matchService.makeAttempt(any(), any()))
                .thenThrow(new SequenceFourRequiredException());

        mockMvc.perform(post("/matches/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sequence": ["RED","BLUE"]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenMatchAlreadyFinished() throws Exception {
        when(matchService.makeAttempt(any(), any()))
                .thenThrow(new MatchAlreadyFinishedException());

        mockMvc.perform(post("/matches/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sequence": ["RED","BLUE","YELLOW","WHITE"]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}