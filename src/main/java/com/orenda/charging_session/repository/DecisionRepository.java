package com.orenda.charging_session.repository;

import com.orenda.charging_session.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {
}

