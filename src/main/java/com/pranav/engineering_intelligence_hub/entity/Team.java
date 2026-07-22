package com.pranav.engineering_intelligence_hub.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    @ManyToMany(mappedBy="teams")
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy="team")
    @Builder.Default
    private Set<Project> projects = new HashSet<>();
}
