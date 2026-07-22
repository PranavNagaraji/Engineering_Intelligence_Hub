package com.pranav.engineering_intelligence_hub.mapper;

import com.pranav.engineering_intelligence_hub.dto.request.DocumentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.DocumentResponse;
import com.pranav.engineering_intelligence_hub.entity.Document;
import com.pranav.engineering_intelligence_hub.entity.Project;
import com.pranav.engineering_intelligence_hub.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentResponse toResponse(@NotNull Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                document.getProject().getId(),
                document.getUploadedBy().getId()
        );
    }

    public Document toEntity(@NotNull DocumentRequest req, Project project, User user) {
        Document document = new Document();
        document.setTitle(req.title());
        document.setContent(req.content());
        document.setProject(project);
        document.setUploadedBy(user);
        return document;
    }
}
