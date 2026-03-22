package com.case_fullstack.mastermind.repositories;

import com.case_fullstack.mastermind.models.entities.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
}
