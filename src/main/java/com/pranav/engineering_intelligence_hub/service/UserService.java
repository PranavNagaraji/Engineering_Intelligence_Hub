package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.entity.Role;
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
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.teamRepository = teamRepository;
    }

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
        user.setRole(Role.MANAGER);
        return userMapper.toResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse demoteToEngineer(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        user.setRole(Role.ENGINEER);
        return userMapper.toResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse assignToTeam(Long userId, Long teamId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        Team team=teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        user.getTeams().add(team);
        return userMapper.toResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse removeFromTeam(Long userId, Long teamId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        Team team=teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        user.getTeams().remove(team);
        return userMapper.toResponse(userRepository.save(user));
    }
}
