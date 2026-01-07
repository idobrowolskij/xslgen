package com.id.xslgen.dto;

import java.time.LocalDateTime;

public record TemplateDto(
        Long id,
        String name,
        String xslUrl,
        String xmlUrl,
        String pdfUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
