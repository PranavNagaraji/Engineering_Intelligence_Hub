package com.pranav.engineering_intelligence_hub.repository;

import com.pranav.engineering_intelligence_hub.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pranav.engineering_intelligence_hub.entity.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProject(Project project);
    boolean existsByTitle(String title);
}