package com.id.xslgen.controller;

import com.id.xslgen.dto.TemplateDto;
import com.id.xslgen.dto.XmlValidationResultDto;
import com.id.xslgen.service.FileStorageService;
import com.id.xslgen.service.TemplateService;
import com.id.xslgen.service.XmlValidationService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {
    private final TemplateService templates;
    private final FileStorageService storage;
    private final XmlValidationService xmlValidation;

    public TemplateController(TemplateService templates, FileStorageService storage, XmlValidationService xmlValidation) {
        this.templates = templates;
        this.storage = storage;
        this.xmlValidation = xmlValidation;
    }

    private String baseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }

    private long userId(Authentication auth) {
        // siehe JWT-Teil: wir setzen principal = UserPrincipal mit id
        return ((com.id.xslgen.security.UserPrincipal) auth.getPrincipal()).id();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TemplateDto> create(
            Authentication auth,
            @RequestParam String name,
            @RequestParam MultipartFile xslFile,
            @RequestParam MultipartFile xmlFile
    ) {
        TemplateDto dto = templates.create(userId(auth), name, xslFile, xmlFile, baseUrl());
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping
    public ResponseEntity<?> list(Authentication auth) {
        return ResponseEntity.ok(templates.list(userId(auth), baseUrl()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDto> get(Authentication auth, @PathVariable long id) {
        return ResponseEntity.ok(templates.get(userId(auth), id, baseUrl()));
    }

    @PutMapping("/{id}/xsl")
    public ResponseEntity<Void> updateXsl(Authentication auth, @PathVariable long id, @RequestBody String xsl) {
        templates.updateXsl(userId(auth), id, xsl);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/xml")
    public ResponseEntity<Void> updateXml(Authentication auth, @PathVariable long id, @RequestBody String xml) {
        templates.updateXml(userId(auth), id, xml);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/transform")
    public ResponseEntity<TemplateDto> transform(Authentication auth, @PathVariable long id) {
        return ResponseEntity.ok(templates.transform(userId(auth), id, baseUrl()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable long id) {
        templates.delete(userId(auth), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/xsl")
    public ResponseEntity<Resource> xsl(Authentication auth, @PathVariable long id) {
        long uid = userId(auth);
        templates.getOwned(uid, id); // Ownership check
        Resource res = storage.asResource(storage.xslPath(uid, id));
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(res);
    }

    @GetMapping("/{id}/xml")
    public ResponseEntity<Resource> xml(Authentication auth, @PathVariable long id) {
        long uid = userId(auth);
        templates.getOwned(uid, id);
        Resource res = storage.asResource(storage.xmlPath(uid, id));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(res);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> pdf(Authentication auth, @PathVariable long id) {
        long uid = userId(auth);
        templates.getOwned(uid, id);
        Resource res = storage.asResource(storage.pdfPath(uid, id));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(res);
    }

    @PostMapping("/validate")
    public ResponseEntity<XmlValidationResultDto> validate(@RequestBody String xml) {
        return xmlValidation.validateXml(xml)
                .map(err -> ResponseEntity.badRequest().body(XmlValidationResultDto.fail(err)))
                .orElseGet(() -> ResponseEntity.ok(XmlValidationResultDto.ok()));
    }
}
