package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.models.dtos.MatchRequestDTO;
import com.case_fullstack.mastermind.models.entities.Attempt;
import com.case_fullstack.mastermind.models.dtos.AttemptResponseDTO;
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

    public Match startMatch(MatchRequestDTO user){
        Match match = new Match();
        List<Colors> correctAnswer = generateCorrectAnswer();
        match.setCorrectAnswer(correctAnswer);
        match.setMatchStatus(MatchStatus.IN_PROGRESS);
        match.setInitialDate(LocalDateTime.now());
        User userMatch = userRepository.findById(user.id())
                        .orElseThrow(() -> new RuntimeException("User not found"));
        match.setUser(userMatch);
        matchRepository.save(match);
        return match;
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
        Match match = optMatch.orElseThrow(() -> new RuntimeException("Match not found!"));

        //2. Validating if match is not in progress
        if (match.getMatchStatus() != MatchStatus.IN_PROGRESS) {
            throw new RuntimeException("Match is already finished!");
        }

        //3. Validating if player still has attempts for this match (<10)
        if (match.getAttempts().size() >= 10) {
            throw new RuntimeException("Match is already finished!");
        }

        //4. Calculating correct positions
        List<Colors> correctAnswer = match.getCorrectAnswer();
        int correctPositions;
        if (sequence != null && sequence.size() == 4) {
            correctPositions = countCorrectPositions(sequence, correctAnswer);
        } else {
            throw new RuntimeException("A sequence of 4 colors is required!");
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
        if (match.getAttempts().size() == correctAnswer.size()) {
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

    public List<Match> findByIdAndStatus(MatchStatus status, Long userId){
        return matchRepository.findMatchesByUserAndStatus(userId, status);
    }

    public Match findMatchById(Long matchId){
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found!"));
        return match;
    }
}
