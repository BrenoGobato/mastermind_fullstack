package com.case_fullstack.mastermind.controllers;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.case_fullstack.mastermind.infra.ControllerExceptionHandler;
import com.case_fullstack.mastermind.models.dtos.MatchRankingDTO;
import com.case_fullstack.mastermind.services.RankingService;

@WebMvcTest(RankingController.class)
@Import(ControllerExceptionHandler.class)
class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RankingService rankingService;

    @Test
    void shouldReturnRanking() throws Exception {

        List<MatchRankingDTO> ranking = List.of(
                new MatchRankingDTO("breno", 1, 120L),
                new MatchRankingDTO("ana", 2, 200L)
        );

        when(rankingService.generateRanking()).thenReturn(ranking);

        mockMvc.perform(get("/ranking"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("breno"))
                .andExpect(jsonPath("$[0].totalAttempts").value(1))
                .andExpect(jsonPath("$[0].durationInSeconds").value(120))
                .andExpect(jsonPath("$[1].username").value("ana"));
    }

    @Test
    void shouldReturnEmptyRanking() throws Exception {

        when(rankingService.generateRanking()).thenReturn(List.of());

        mockMvc.perform(get("/ranking"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }
}