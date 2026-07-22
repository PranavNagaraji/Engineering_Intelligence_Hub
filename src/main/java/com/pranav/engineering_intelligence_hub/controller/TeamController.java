package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.TeamRequest;
import com.pranav.engineering_intelligence_hub.dto.response.TeamResponse;
import com.pranav.engineering_intelligence_hub.entity.Team;
import com.pranav.engineering_intelligence_hub.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public TeamResponse createTeam(@RequestBody TeamRequest teamRequest){
        return teamService.createTeam(teamRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){
        teamService.deleteTeam(id);
    }
}
