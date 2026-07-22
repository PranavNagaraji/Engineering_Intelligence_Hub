package com.pranav.engineering_intelligence_hub.repository;
import com.pranav.engineering_intelligence_hub.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pranav.engineering_intelligence_hub.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTeam(Team team);
}
