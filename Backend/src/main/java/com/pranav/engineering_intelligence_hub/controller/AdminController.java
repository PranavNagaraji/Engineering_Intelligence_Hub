package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.response.DashboardResponse;
import com.pranav.engineering_intelligence_hub.dto.response.UserResponse;
import com.pranav.engineering_intelligence_hub.service.DashboardService;
import com.pranav.engineering_intelligence_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final DashboardService dashboardService;

    @PostMapping("/users/{id}/promote")
    public UserResponse promoteToManager(@PathVariable Long id){
        return userService.promoteToManager(id);
    }

    @PostMapping("/users/{id}/demote")
    public UserResponse demoteToEngineer(@PathVariable Long id){
        return userService.demoteToEngineer(id);
    }

    @PostMapping("/users/{userId}/teams/{teamId}")
    public UserResponse assignToTeam(@PathVariable Long userId, @PathVariable Long teamId){
        return userService.assignToTeam(userId, teamId);
    }

    @DeleteMapping("/users/{userId}/teams/{teamId}")
    public UserResponse removeFromTeam(@PathVariable Long userId, @PathVariable Long teamId){
        return userService.removeFromTeam(userId, teamId);
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(){
        return dashboardService.getDashboard();
    }
}
