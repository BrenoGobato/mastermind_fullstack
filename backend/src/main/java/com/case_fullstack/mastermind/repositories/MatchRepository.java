package com.case_fullstack.mastermind.repositories;

import com.case_fullstack.mastermind.models.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
