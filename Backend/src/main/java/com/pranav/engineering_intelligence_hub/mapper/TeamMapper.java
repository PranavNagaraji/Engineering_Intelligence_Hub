package com.pranav.engineering_intelligence_hub.mapper;

import com.pranav.engineering_intelligence_hub.dto.request.TeamRequest;
import com.pranav.engineering_intelligence_hub.dto.response.TeamResponse;
import com.pranav.engineering_intelligence_hub.entity.Team;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamResponse toResponse(@Nonnull Team team){
        return new TeamResponse(
                team.getId(),
                team.getName()
        );
    }

    public Team toEntity(@Nonnull TeamRequest teamRequest){
        Team team = new Team();
        team.setName(teamRequest.name());
        return team;
    }
}
