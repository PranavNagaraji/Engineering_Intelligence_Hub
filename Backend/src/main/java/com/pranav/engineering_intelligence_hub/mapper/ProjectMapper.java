package com.pranav.engineering_intelligence_hub.mapper;

import com.pranav.engineering_intelligence_hub.dto.request.ProjectRequest;
import com.pranav.engineering_intelligence_hub.dto.response.ProjectResponse;
import com.pranav.engineering_intelligence_hub.entity.Project;
import com.pranav.engineering_intelligence_hub.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getTeam().getId());
    }

    public Project toEntity(ProjectRequest projectRequest, Team team) {
        Project project = new Project();
        project.setName(projectRequest.name());
        project.setDescription(projectRequest.description());
        project.setTeam(team);
        return project;
    }
}
