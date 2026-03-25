package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.infra.exceptions.MatchAlreadyFinishedException;
import com.case_fullstack.mastermind.infra.exceptions.MatchNotFoundException;
import com.case_fullstack.mastermind.infra.exceptions.SequenceFourRequiredException;
import com.case_fullstack.mastermind.infra.exceptions.UserNotFoundException;
import com.case_fullstack.mastermind.models.dtos.*;
import com.case_fullstack.mastermind.models.entities.Attempt;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.entities.User;
import com.case_fullstack.mastermind.models.enums.Colors;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.repositories.AttemptRepository;
import com.case_fullstack.mastermind.repositories.MatchRepository;
import com.case_fullstack.mastermind.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private UserRepository userRepository;

    public MatchResponseDTO startMatch(MatchRequestDTO matchRequestDTO){
        Match match = new Match();

        List<Colors> correctAnswer = generateCorrectAnswer();
        match.setCorrectAnswer(correctAnswer);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setInitialDate(LocalDateTime.now());

        User userMatch = userRepository.findById(matchRequestDTO.userId())
                .orElseThrow(UserNotFoundException::new);

        match.setUser(userMatch);

        Match savedMatch = matchRepository.save(match);

        return new MatchResponseDTO(
                savedMatch.getId(),
                savedMatch.getMatchStatus(),
                savedMatch.getInitialDate(),
                savedMatch.getFinalDate()
        );
    }

    public List<Colors> generateCorrectAnswer(){
        List<Colors> colors = new ArrayList<>(Arrays.asList(Colors.values()));
        Collections.shuffle(colors);
        return new ArrayList<>(colors.subList(0, 4));
    }

    public AttemptResponseDTO makeAttempt(Long matchId, List<Colors> sequence) {
        //Steps for a new attempt

        //1. Verifying if match exists according to the ID
        Optional<Match> optMatch = matchRepository.findById(matchId);
        Match match = optMatch.orElseThrow(MatchNotFoundException::new);

        //2. Validating if match is not in progress
        if (match.getMatchStatus() != MatchStatus.IN_PROGRESS) {
            throw new MatchAlreadyFinishedException();
        }

        //3. Validating if player still has attempts for this match (<10)
        if (match.getAttempts().size() >= 10) {
            throw new MatchAlreadyFinishedException();
        }

        //4. Calculating correct positions
        List<Colors> correctAnswer = match.getCorrectAnswer();
        int correctPositions;
        if (sequence != null && sequence.size() == 4) {
            correctPositions = countCorrectPositions(sequence, correctAnswer);
        } else {
            throw new SequenceFourRequiredException();
        }

        //5. Creating a new attempt with required data
        Attempt attempt = new Attempt();
        attempt.setMatch(match);
        attempt.setSequence(sequence);
        attempt.setCorrectPositions(correctPositions);

        //6. Associating the attempt to the match
        match.getAttempts().add(attempt);

        //7. Saving attempt
        attemptRepository.save(attempt);

        //8. Verifying if users has won or lost
        if (correctPositions == correctAnswer.size()) {
            match.setMatchStatus(MatchStatus.VICTORY);
            match.setFinalDate(LocalDateTime.now());
        } else if (match.getAttempts().size() == 10) {
            match.setMatchStatus(MatchStatus.DEFEAT);
            match.setFinalDate(LocalDateTime.now());
        }

        //9. Counting how many attempts are left
        int attemptsLeft = 10 - match.getAttempts().size();

        //10. Saves the match
        matchRepository.save(match);

        //11. Return
        if (match.getMatchStatus().equals(MatchStatus.VICTORY) || match.getMatchStatus().equals(MatchStatus.DEFEAT)) {
            return new AttemptResponseDTO(correctPositions, match.getMatchStatus(), attemptsLeft, match.getCorrectAnswer());
        } else {
            return new AttemptResponseDTO(correctPositions, match.getMatchStatus(), attemptsLeft);
        }
    }

    public Integer countCorrectPositions(List<Colors> sequence, List<Colors> correctAnswer){
        int correctCount = 0;
        int size = Math.min(sequence.size(), correctAnswer.size());
        for(int i = 0; i < size; i++){
            if (sequence.get(i).equals(correctAnswer.get(i))){
                correctCount++;
            }
        }
        return correctCount;
    }

    public List<MatchResponseDTO> findByIdAndStatus(MatchStatus status, Long userId) {
        List<Match> matches = matchRepository.findByUserIdAndMatchStatus(userId, status);

        return matches.stream()
                .map(match -> new MatchResponseDTO(
                        match.getId(),
                        match.getMatchStatus(),
                        match.getInitialDate(),
                        match.getFinalDate()
                ))
                .toList();
    }

    public MatchDetailsResponseDTO findMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);

        List<AttemptDetailsDTO> attempts = match.getAttempts().stream()
                .map(attempt -> new AttemptDetailsDTO(
                        attempt.getSequence(),
                        attempt.getCorrectPositions()
                ))
                .toList();

        List<Colors> correctAnswer = null;

        if (match.getMatchStatus() == MatchStatus.VICTORY || match.getMatchStatus() == MatchStatus.DEFEAT) {
            correctAnswer = match.getCorrectAnswer();
        }

        return new MatchDetailsResponseDTO(
                match.getId(),
                match.getMatchStatus(),
                match.getInitialDate(),
                match.getFinalDate(),
                attempts,
                correctAnswer
        );
    }
}
