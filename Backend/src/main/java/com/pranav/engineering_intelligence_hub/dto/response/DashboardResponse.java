package com.pranav.engineering_intelligence_hub.dto.response;

public record DashboardResponse(
        Long users,
        Long teams,
        Long projects,
        Long documents,
        Long openIncidents,
        Long inProgressIncidents,
        Long resolvedIncidents,
        Long closedIncidents
) {
}
