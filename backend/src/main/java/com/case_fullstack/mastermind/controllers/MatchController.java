package com.case_fullstack.mastermind.controllers;

import com.case_fullstack.mastermind.models.dtos.AttemptRequestDTO;
import com.case_fullstack.mastermind.models.dtos.AttemptResponseDTO;
import com.case_fullstack.mastermind.models.dtos.MatchRequestDTO;
import com.case_fullstack.mastermind.models.dtos.MatchResponseDTO;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    //Starting a new match
    @PostMapping
    public ResponseEntity<MatchResponseDTO> startMatch(@RequestBody MatchRequestDTO matchRequestDTO){
        MatchResponseDTO newMatch = matchService.startMatch(matchRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatch);
    }

    //Opening the game by id
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable Long id){
        MatchResponseDTO match = matchService.findMatchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(match);
    }

    //Option in the menu to see all games by status
    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByStatus(
            @RequestParam MatchStatus status,
            @RequestParam Long userId
    ) {
        List<MatchResponseDTO> matches = matchService.findByIdAndStatus(status, userId);
        return ResponseEntity.ok(matches);
    }

    //Making an attempt
    @PostMapping("/{id}/attempts")
    public ResponseEntity<AttemptResponseDTO> attemptMatch(@PathVariable Long id, @RequestBody AttemptRequestDTO attemptRequestDTO){
        AttemptResponseDTO attempt = matchService.makeAttempt(id, attemptRequestDTO.sequence());
        return ResponseEntity.status(HttpStatus.CREATED).body(attempt);
    }
}
