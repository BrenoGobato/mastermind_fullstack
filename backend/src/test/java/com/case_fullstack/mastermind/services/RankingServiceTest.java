package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.models.dtos.MatchRankingDTO;
import com.case_fullstack.mastermind.models.entities.Attempt;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.entities.User;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.repositories.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    void shouldGenerateRankingOrderedByTotalAttemptsAndDuration() {
        Match match1 = buildVictoryMatch("breno", 2, 120);
        Match match2 = buildVictoryMatch("ana", 1, 300);
        Match match3 = buildVictoryMatch("carlos", 1, 180);

        when(matchRepository.findByMatchStatus(MatchStatus.VICTORY))
                .thenReturn(List.of(match1, match2, match3));

        List<MatchRankingDTO> ranking = rankingService.generateRanking();

        assertNotNull(ranking);
        assertEquals(3, ranking.size());

        assertEquals("carlos", ranking.get(0).getUsername());
        assertEquals(1, ranking.get(0).getTotalAttempts());
        assertEquals(180L, ranking.get(0).getDurationInSeconds());

        assertEquals("ana", ranking.get(1).getUsername());
        assertEquals(1, ranking.get(1).getTotalAttempts());
        assertEquals(300L, ranking.get(1).getDurationInSeconds());

        assertEquals("breno", ranking.get(2).getUsername());
        assertEquals(2, ranking.get(2).getTotalAttempts());
        assertEquals(120L, ranking.get(2).getDurationInSeconds());

        verify(matchRepository).findByMatchStatus(MatchStatus.VICTORY);
    }

    @Test
    void shouldReturnEmptyRankingWhenThereAreNoVictories() {
        when(matchRepository.findByMatchStatus(MatchStatus.VICTORY))
                .thenReturn(List.of());

        List<MatchRankingDTO> ranking = rankingService.generateRanking();

        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());

        verify(matchRepository).findByMatchStatus(MatchStatus.VICTORY);
    }

    private Match buildVictoryMatch(String username, int attemptsCount, long durationInSeconds) {
        User user = new User();
        user.setUsername(username);

        Match match = new Match();
        match.setUser(user);
        match.setMatchStatus(MatchStatus.VICTORY);
        match.setInitialDate(LocalDateTime.of(2026, 3, 25, 10, 0, 0));
        match.setFinalDate(match.getInitialDate().plusSeconds(durationInSeconds));

        List<Attempt> attempts = java.util.stream.IntStream.range(0, attemptsCount)
                .mapToObj(i -> {
                    Attempt attempt = new Attempt();
                    attempt.setMatch(match);
                    return attempt;
                })
                .toList();

        match.setAttempts(attempts);

        return match;
    }
}