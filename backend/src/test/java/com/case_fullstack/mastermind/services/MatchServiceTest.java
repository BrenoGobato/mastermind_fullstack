package com.case_fullstack.mastermind.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.case_fullstack.mastermind.infra.exceptions.MatchAlreadyFinishedException;
import com.case_fullstack.mastermind.infra.exceptions.MatchNotFoundException;
import com.case_fullstack.mastermind.infra.exceptions.SequenceFourRequiredException;
import com.case_fullstack.mastermind.infra.exceptions.UserNotFoundException;
import com.case_fullstack.mastermind.models.dtos.AttemptResponseDTO;
import com.case_fullstack.mastermind.models.dtos.MatchRequestDTO;
import com.case_fullstack.mastermind.models.dtos.MatchResponseDTO;
import com.case_fullstack.mastermind.models.entities.Attempt;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.entities.User;
import com.case_fullstack.mastermind.models.enums.Colors;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.repositories.AttemptRepository;
import com.case_fullstack.mastermind.repositories.MatchRepository;
import com.case_fullstack.mastermind.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private AttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void shouldStartMatchSuccessfully() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("breno");
        user.setEmail("breno@email.com");

        Match savedMatch = new Match();
        savedMatch.setId(10L);
        savedMatch.setMatchStatus(MatchStatus.IN_PROGRESS);
        savedMatch.setInitialDate(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

        MatchResponseDTO response = matchService.startMatch(new MatchRequestDTO(userId));

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(MatchStatus.IN_PROGRESS, response.matchStatus());
        assertNotNull(response.initialDate());
        assertNull(response.finalDate());

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchCaptor.capture());

        Match matchToSave = matchCaptor.getValue();
        assertEquals(MatchStatus.IN_PROGRESS, matchToSave.getMatchStatus());
        assertNotNull(matchToSave.getInitialDate());
        assertEquals(user, matchToSave.getUser());
        assertNotNull(matchToSave.getCorrectAnswer());
        assertEquals(4, matchToSave.getCorrectAnswer().size());
        assertEquals(4, matchToSave.getCorrectAnswer().stream().distinct().count());
    }

    @Test
    void shouldThrowWhenStartingMatchWithNonexistentUser() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> matchService.startMatch(new MatchRequestDTO(userId)));

        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void shouldGenerateCorrectAnswerWithFourUniqueColors() {
        List<Colors> answer = matchService.generateCorrectAnswer();

        assertNotNull(answer);
        assertEquals(4, answer.size());
        assertEquals(4, answer.stream().distinct().count());
    }

    @Test
    void shouldCountCorrectPositions() {
        List<Colors> sequence = List.of(
                Colors.RED,
                Colors.BLUE,
                Colors.YELLOW,
                Colors.WHITE
        );

        List<Colors> correctAnswer = List.of(
                Colors.RED,
                Colors.BLACK,
                Colors.YELLOW,
                Colors.BROWN
        );

        Integer result = matchService.countCorrectPositions(sequence, correctAnswer);

        assertEquals(2, result);
    }

    @Test
    void shouldMakeAttemptAndSetVictoryWhenAllPositionsAreCorrect() {
        Long matchId = 1L;

        Match match = new Match();
        match.setId(matchId);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setInitialDate(LocalDateTime.now());
        match.setAttempts(new ArrayList<>());
        match.setCorrectAnswer(List.of(
                Colors.RED,
                Colors.BLUE,
                Colors.YELLOW,
                Colors.WHITE
        ));

        List<Colors> attemptSequence = List.of(
                Colors.RED,
                Colors.BLUE,
                Colors.YELLOW,
                Colors.WHITE
        );

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(attemptRepository.save(any(Attempt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttemptResponseDTO response = matchService.makeAttempt(matchId, attemptSequence);

        assertNotNull(response);
        assertEquals(4, response.getCorrectPositions());
        assertEquals(MatchStatus.VICTORY, response.getMatchStatus());
        assertEquals(9, response.getAttemptsLeft());
        assertEquals(match.getCorrectAnswer(), response.getCorrectAnswer());

        assertEquals(MatchStatus.VICTORY, match.getMatchStatus());
        assertNotNull(match.getFinalDate());
        assertEquals(1, match.getAttempts().size());

        verify(attemptRepository).save(any(Attempt.class));
        verify(matchRepository).save(match);
    }

    @Test
    void shouldMakeAttemptAndKeepMatchInProgressWhenNotLastAttemptAndNotVictory() {
        Long matchId = 2L;

        Match match = new Match();
        match.setId(matchId);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setInitialDate(LocalDateTime.now());
        match.setAttempts(new ArrayList<>());
        match.setCorrectAnswer(List.of(
                Colors.RED,
                Colors.BLUE,
                Colors.YELLOW,
                Colors.WHITE
        ));

        List<Colors> attemptSequence = List.of(
                Colors.BLACK,
                Colors.BROWN,
                Colors.YELLOW,
                Colors.ORANGE
        );

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(attemptRepository.save(any(Attempt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttemptResponseDTO response = matchService.makeAttempt(matchId, attemptSequence);

        assertEquals(1, response.getCorrectPositions());
        assertEquals(MatchStatus.IN_PROGRESS, response.getMatchStatus());
        assertEquals(9, response.getAttemptsLeft());
        assertNull(response.getCorrectAnswer());

        assertEquals(MatchStatus.IN_PROGRESS, match.getMatchStatus());
        assertNull(match.getFinalDate());
        assertEquals(1, match.getAttempts().size());
    }

    @Test
    void shouldThrowWhenMatchDoesNotExist() {
        Long matchId = 123L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class,
                () -> matchService.makeAttempt(matchId, List.of(
                        Colors.RED, Colors.BLUE, Colors.YELLOW, Colors.WHITE
                )));

        verify(attemptRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenMatchIsAlreadyFinished() {
        Long matchId = 3L;

        Match match = new Match();
        match.setId(matchId);
        match.setMatchStatus(MatchStatus.VICTORY);
        match.setAttempts(new ArrayList<>());
        match.setCorrectAnswer(List.of(
                Colors.RED, Colors.BLUE, Colors.YELLOW, Colors.WHITE
        ));

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        assertThrows(MatchAlreadyFinishedException.class,
                () -> matchService.makeAttempt(matchId, List.of(
                        Colors.RED, Colors.BLUE, Colors.YELLOW, Colors.WHITE
                )));

        verify(attemptRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenSequenceDoesNotHaveFourColors() {
        Long matchId = 4L;

        Match match = new Match();
        match.setId(matchId);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setAttempts(new ArrayList<>());
        match.setCorrectAnswer(List.of(
                Colors.RED, Colors.BLUE, Colors.YELLOW, Colors.WHITE
        ));

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        assertThrows(SequenceFourRequiredException.class,
                () -> matchService.makeAttempt(matchId, List.of(
                        Colors.RED, Colors.BLUE, Colors.YELLOW
                )));

        verify(attemptRepository, never()).save(any());
    }

    @Test
    void shouldSetDefeatOnTenthAttempt() {
        Long matchId = 5L;

        Match match = new Match();
        match.setId(matchId);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setInitialDate(LocalDateTime.now());
        match.setCorrectAnswer(List.of(
                Colors.RED, Colors.BLUE, Colors.YELLOW, Colors.WHITE
        ));

        List<Attempt> previousAttempts = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Attempt oldAttempt = new Attempt();
            oldAttempt.setSequence(List.of(
                    Colors.BLACK, Colors.BROWN, Colors.ORANGE, Colors.RED
            ));
            oldAttempt.setCorrectPositions(0);
            oldAttempt.setMatch(match);
            previousAttempts.add(oldAttempt);
        }
        match.setAttempts(previousAttempts);

        List<Colors> newSequence = List.of(
                Colors.BLACK, Colors.BROWN, Colors.ORANGE, Colors.RED
        );

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(attemptRepository.save(any(Attempt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttemptResponseDTO response = matchService.makeAttempt(matchId, newSequence);

        assertEquals(0, response.getCorrectPositions());
        assertEquals(MatchStatus.DEFEAT, response.getMatchStatus());
        assertEquals(0, response.getAttemptsLeft());
        assertEquals(match.getCorrectAnswer(), response.getCorrectAnswer());

        assertEquals(MatchStatus.DEFEAT, match.getMatchStatus());
        assertNotNull(match.getFinalDate());
        assertEquals(10, match.getAttempts().size());
    }
}