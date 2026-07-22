package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.IncidentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.IncidentResponse;
import com.pranav.engineering_intelligence_hub.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    @PostMapping("/projects/{projectId}")
    public IncidentResponse createIncident(@RequestBody IncidentRequest req, @PathVariable Long projectId){
        return incidentService.createIncident(projectId, req, req.assignedEngineerUsername());
    }

    @GetMapping("/projects/{projectId}")
    public List<IncidentResponse> getIncidentsByProjectId(@PathVariable Long projectId){
        return incidentService.getIncidentsByProjectId(projectId);
    }

    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable Long id){
        incidentService.deleteIncidentById(id);
    }

    @PatchMapping("/{id}/start")
    public IncidentResponse startIncident(@PathVariable Long id){
        return incidentService.startIncident(id);
    }

    @PatchMapping("/{id}/resolve")
    public IncidentResponse resolveIncident(@PathVariable Long id){
        return incidentService.resolveIncident(id);
    }

    @PatchMapping("/{id}/close")
    public IncidentResponse closeIncident(@PathVariable Long id){
        return incidentService.closeIncident(id);
    }
}
