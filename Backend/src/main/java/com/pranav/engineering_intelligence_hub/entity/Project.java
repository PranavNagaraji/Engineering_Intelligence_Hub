package com.pranav.engineering_intelligence_hub.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="projects")
public class Project {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String description;

    @OneToMany(mappedBy="project")
    @Builder.Default
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy="project")
    @Builder.Default
    private Set<Incident> incidents = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;
}
