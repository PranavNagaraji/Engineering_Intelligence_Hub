package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.DocumentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.DocumentResponse;
import com.pranav.engineering_intelligence_hub.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/projects/{projectId}")
    public DocumentResponse createDocument(@PathVariable Long projectId, @Valid @RequestBody DocumentRequest documentRequest){
        return documentService.createDocument(projectId, documentRequest);
    }

    @GetMapping("/projects/{projectId}")
    public List<DocumentResponse> getDocumentsByProjectId(@PathVariable Long projectId){
        return documentService.getDocumentsByProjectId(projectId);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id){
        documentService.deleteDocumentsById(id);
    }
}
