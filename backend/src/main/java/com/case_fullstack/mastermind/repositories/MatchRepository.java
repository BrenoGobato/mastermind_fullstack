package com.case_fullstack.mastermind.repositories;

import com.case_fullstack.mastermind.models.dtos.MatchResponseDTO;
import com.case_fullstack.mastermind.models.entities.Match;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUserIdAndMatchStatus(Long userId, MatchStatus matchStatus);
    List<Match> findByMatchStatus(MatchStatus matchStatus);
}