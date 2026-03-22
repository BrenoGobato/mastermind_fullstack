package com.case_fullstack.mastermind.controllers;

import com.case_fullstack.mastermind.models.dtos.MatchRankingDTO;
import com.case_fullstack.mastermind.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<MatchRankingDTO>> getRanking() {
         List<MatchRankingDTO> ranking = rankingService.generateRanking();
         return ResponseEntity.status(HttpStatus.OK).body(ranking);
    }

}
