package com.id.xslgen.dto;

import com.id.xslgen.model.Template;

public class TemplateMapper {
    private TemplateMapper() {}

    public static TemplateDto toDto(Template t, String baseUrl) {
        long id = t.getId();
        return new TemplateDto(
                id,
                t.getName(),
                baseUrl + "/api/templates/" + id + "/xsl",
                baseUrl + "/api/templates/" + id + "/xml",
                baseUrl + "/api/templates/" + id + "/pdf",
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
