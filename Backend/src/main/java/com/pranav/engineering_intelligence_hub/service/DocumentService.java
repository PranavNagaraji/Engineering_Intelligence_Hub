package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.request.DocumentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.DocumentResponse;
import com.pranav.engineering_intelligence_hub.entity.*;
import com.pranav.engineering_intelligence_hub.exceptions.AccessDeniedException;
import com.pranav.engineering_intelligence_hub.exceptions.DocumentAlreadyFoundException;
import com.pranav.engineering_intelligence_hub.exceptions.DocumentNotFoundException;
import com.pranav.engineering_intelligence_hub.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.pranav.engineering_intelligence_hub.exceptions.ProjectNotFoundException;
import com.pranav.engineering_intelligence_hub.repository.DocumentRepository;
import com.pranav.engineering_intelligence_hub.repository.ProjectRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final DocumentMapper documentMapper;
    private final AuditService auditService;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public DocumentResponse createDocument(Long projectId, DocumentRequest req) {
        Project project=projectRepository.findById(projectId)
            .orElseThrow(()-> new ProjectNotFoundException(projectId));
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.getTeams().contains(project.getTeam())){
            throw new AccessDeniedException("You are not a member of this team");
        }
        if(documentRepository.existsByTitle(req.title())){
            throw new DocumentAlreadyFoundException(req.title());
        }
        Document document=documentMapper.toEntity(req, project, currentUser);
        Document savedDocument = documentRepository.save(document);
        auditService.log(
          AuditEvent.DOCUMENT_CREATED,
          currentUser.getUsername(),
          "Created document: "+savedDocument.getTitle()
        );
        return documentMapper.toResponse(savedDocument);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public List<DocumentResponse> getDocumentsByProjectId(Long projectId) {
        Project project=projectService.getProjectEntity(projectId);
        return documentRepository
                .findByProject(project)
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public void deleteDocumentsById(Long documentId) {
        Document document=documentRepository.findById(documentId).orElseThrow(()->new DocumentNotFoundException(documentId));
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !document.getUploadedBy().equals(currentUser)){
            throw new AccessDeniedException("You are not authorized to make any changes to this document");
        }
        documentRepository.deleteById(documentId);
        auditService.log(
            AuditEvent.DOCUMENT_DELETED,
                currentUser.getUsername(),
                "Deleted document: "+document.getTitle()
        );
    }
}
