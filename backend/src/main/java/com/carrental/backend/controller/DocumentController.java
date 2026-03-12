package com.carrental.backend.controller;

import com.carrental.backend.dto.request.UploadDocumentRequest;
import com.carrental.backend.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestBody UploadDocumentRequest request) {
        return ResponseEntity.ok(documentService.uploadDocument(request));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<?> verifyDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.verifyDocument(id));
    }
}
