package com.pranav.engineering_intelligence_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranav.engineering_intelligence_hub.entity.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);
}
