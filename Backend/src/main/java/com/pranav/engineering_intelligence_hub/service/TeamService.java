package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.request.TeamRequest;
import com.pranav.engineering_intelligence_hub.dto.response.TeamResponse;
import com.pranav.engineering_intelligence_hub.entity.AuditEvent;
import com.pranav.engineering_intelligence_hub.entity.User;
import com.pranav.engineering_intelligence_hub.exceptions.TeamAlreadyFoundException;
import com.pranav.engineering_intelligence_hub.exceptions.TeamNotFoundException;
import com.pranav.engineering_intelligence_hub.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.pranav.engineering_intelligence_hub.entity.Team;
import com.pranav.engineering_intelligence_hub.repository.TeamRepository;

@Service
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class TeamService {

    private final UserService userService;
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final AuditService auditService;

    public TeamResponse createTeam(TeamRequest req){
        if(teamRepository.existsByName(req.name()))
            throw new TeamAlreadyFoundException(req.name());
        Team team = teamMapper.toEntity(req);
        User currentUser=userService.getCurrentUser();
        teamRepository.save(team);
        auditService.log(AuditEvent.TEAM_CREATED, currentUser.getUsername(), "Created team: "+team.getName());
        return teamMapper.toResponse(team);
    }

    public void deleteTeam(Long teamId){
        Team team=teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        teamRepository.delete(team);
    }
}
