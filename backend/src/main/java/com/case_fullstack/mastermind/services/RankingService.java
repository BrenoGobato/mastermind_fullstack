package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.models.dtos.MatchRankingDTO;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.case_fullstack.mastermind.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private MatchRepository matchRepository;

    public List<MatchRankingDTO> generateRanking(){
        List<Match> matches = matchRepository.findByMatchStatus(MatchStatus.VICTORY);

        List<MatchRankingDTO> ranking = matches.stream()
                .map(match -> new MatchRankingDTO(
                        match.getUser().getUsername(),
                        match.getAttempts().size(),
                        Duration.between(match.getInitialDate(), match.getFinalDate()).getSeconds()
                ))
                .sorted(Comparator
                        .comparingInt(MatchRankingDTO::getTotalAttempts)
                        .thenComparing(MatchRankingDTO::getDurationInSeconds)
                )
                .collect(Collectors.toList()
                );

        return ranking;
    }
}
