package com.carrental.backend.service;

import com.carrental.backend.dto.request.UploadDocumentRequest;
import com.carrental.backend.dto.response.AuthResponse;
import com.carrental.backend.entity.Document;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.DocumentStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.DocumentRepository;
import com.carrental.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final UserRepository userRepository;

    private final DocumentRepository documentRepository;

    public DocumentService(UserRepository userRepository, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
    }

    private User getAuthenticatedUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthResponse uploadDocument(UploadDocumentRequest request){
        User user = getAuthenticatedUser();

        Document document = new Document();

        document.setUser(user);
        document.setType(request.getType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setStatus(DocumentStatus.PENDING);

        documentRepository.save(document);

        return new AuthResponse("Document uploaded for Verification");

    }

    @Transactional
    public AuthResponse verifyDocument(Long documentId){
        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Only Admin can verify documents");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setStatus(DocumentStatus.VERIFIED);

        documentRepository.save(document);

        return new AuthResponse("Document verified successfully");
    }
}
