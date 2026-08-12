package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.ProjectRequest;
import com.pranav.engineering_intelligence_hub.dto.response.ProjectResponse;
import com.pranav.engineering_intelligence_hub.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse createProject(@Valid @RequestBody ProjectRequest req) {
        return projectService.createProject(req);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @GetMapping("/teams/{teamId}")
    public List<ProjectResponse> getProjects(@PathVariable Long teamId) {
        return projectService.getAllProjectsByTeamId(teamId);
    }
}
