package com.pranav.engineering_intelligence_hub.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pranav.engineering_intelligence_hub.service.ProjectService;
import com.pranav.engineering_intelligence_hub.security.JwtService;

@Component
public class TempController implements CommandLineRunner{

    private ProjectService projectService;
    private JwtService jwtService;
    public TempController(ProjectService projectService, JwtService jwtService) {
        this.projectService = projectService;
        this.jwtService = jwtService;
    }

    @Override
    public void run(String... args) {
//        System.out.println(
//                new BCryptPasswordEncoder()
//                        .encode("Pranav123")
//        );
//        System.out.println(jwtService.generateToken("Pranav"));
    }

}