package com.carrental.backend.dto.request;

import com.carrental.backend.entity.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDocumentRequest {

    private DocumentType type;

    private String documentNumber;
}
