package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.entity.AuditEvent;
import com.pranav.engineering_intelligence_hub.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pranav.engineering_intelligence_hub.dto.response.UserResponse;
import com.pranav.engineering_intelligence_hub.entity.Team;
import com.pranav.engineering_intelligence_hub.entity.User;
import com.pranav.engineering_intelligence_hub.exceptions.TeamNotFoundException;
import com.pranav.engineering_intelligence_hub.exceptions.UserNotFoundException;
import com.pranav.engineering_intelligence_hub.mapper.UserMapper;
import com.pranav.engineering_intelligence_hub.repository.TeamRepository;
import com.pranav.engineering_intelligence_hub.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TeamRepository teamRepository;
    private final AuditService auditService;

    public UserResponse getUserById(Long id){
        User user= userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    public User getCurrentUser(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        return userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse promoteToManager(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        if(!user.getRole().equals(Role.ENGINEER)){
            throw new IllegalStateException("Only Engineers can be promoted to Manager");
        }
        user.setRole(Role.MANAGER);
        User currentUser=getCurrentUser();
        User savedUser=userRepository.save(user);
        auditService.log(
                AuditEvent.USER_PROMOTED,
                currentUser.getUsername(),
                "Engineer promoted to Manager: "+savedUser.getUsername()
        );
        return userMapper.toResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse demoteToEngineer(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        if (user.getRole() != Role.MANAGER) {
            throw new IllegalStateException("Only Managers can be demoted to Engineer");
        }
        user.setRole(Role.ENGINEER);
        User savedUser=userRepository.save(user);
        User currentUser=getCurrentUser();
        auditService.log(
                AuditEvent.USER_DEMOTED,
                currentUser.getUsername(),
                "Engineer demoted: "+savedUser.getUsername()
        );
        return userMapper.toResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse assignToTeam(Long userId, Long teamId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        Team team=teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        user.getTeams().add(team);
        User currentUser=getCurrentUser();
        User savedUser=userRepository.save(user);
        auditService.log(
                AuditEvent.USER_ASSIGNED_TO_TEAM,
                currentUser.getUsername(),
                "Engineer assigned to team: "+savedUser.getUsername()
        );
        return userMapper.toResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse removeFromTeam(Long userId, Long teamId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        Team team=teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        user.getTeams().remove(team);
        User savedUser=userRepository.save(user);
        User currentUser=getCurrentUser();
        auditService.log(
                AuditEvent.USER_REMOVED_FROM_TEAM,
                currentUser.getUsername(),
                "Engineer removed from team: "+savedUser.getUsername()
        );
        return userMapper.toResponse(savedUser);
    }
}
