package com.id.xslgen.service;

import com.id.xslgen.dto.TemplateDto;
import com.id.xslgen.dto.TemplateMapper;
import com.id.xslgen.model.Template;
import com.id.xslgen.model.User;
import com.id.xslgen.repository.TemplateRepository;
import com.id.xslgen.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepository templates;
    private final UserRepository users;
    private final FileStorageService storage;
    private final TransformationService transformer;

    public TemplateService(TemplateRepository templates, UserRepository users, FileStorageService storage, TransformationService transformer) {
        this.templates = templates;
        this.users = users;
        this.storage = storage;
        this.transformer = transformer;
    }

    @Transactional
    public TemplateDto create(long userId, String name, MultipartFile xslFile, MultipartFile xmlFile, String baseUrl) {
        User owner = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Template t = new Template();
        t.setName(name);
        t.setOwner(owner);
        t = templates.save(t);

        try {
            storage.save(userId, t.getId(), storage.xslPath(userId, t.getId()), xslFile.getInputStream());
            storage.save(userId, t.getId(), storage.xmlPath(userId, t.getId()), xmlFile.getInputStream());

            transformer.generatePdf(
                    storage.xslPath(userId, t.getId()),
                    storage.xmlPath(userId, t.getId()),
                    storage.pdfPath(userId, t.getId())
            );

            return TemplateMapper.toDto(t, baseUrl);
        } catch (IOException e) {
            throw new RuntimeException("Create template failed", e);
        }
    }

    @Transactional(readOnly = true)
    public List<TemplateDto> list(long userId, String baseUrl) {
        return templates.findAllByOwner_Id(userId).stream()
                .map(t -> TemplateMapper.toDto(t, baseUrl))
                .toList();
    }

    @Transactional(readOnly = true)
    public Template getOwned(long userId, long templateId) {
        return templates.findByIdAndOwner_Id(templateId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Template not found"));
    }

    @Transactional(readOnly = true)
    public TemplateDto get(long userId, long templateId, String baseUrl) {
        return TemplateMapper.toDto(getOwned(userId, templateId), baseUrl);
    }

    @Transactional
    public void updateXsl(long userId, long templateId, String xsl) {
        Template t = getOwned(userId, templateId);
        try {
            storage.writeString(userId, templateId, storage.xslPath(userId, templateId), xsl);
            templates.save(t);
        } catch (IOException e) {
            throw new RuntimeException("Saving XSL failed", e);
        }
    }

    @Transactional
    public void updateXml(long userId, long templateId, String xml) {
        Template t = getOwned(userId, templateId);
        try {
            storage.writeString(userId, templateId, storage.xmlPath(userId, templateId), xml);
            templates.save(t);
        } catch (IOException e) {
            throw new RuntimeException("Saving XML failed", e);
        }
    }

    @Transactional
    public TemplateDto transform(long userId, long templateId, String baseUrl) {
        Template t = getOwned(userId, templateId);

        transformer.generatePdf(
                storage.xslPath(userId, templateId),
                storage.xmlPath(userId, templateId),
                storage.pdfPath(userId, templateId)
        );

        templates.save(t);

        return TemplateMapper.toDto(t, baseUrl);
    }

    @Transactional
    public void delete(long userId, long templateId) {
        Template t = getOwned(userId, templateId);
        try {
            storage.deleteTemplateDir(userId, templateId);
            templates.delete(t);
        } catch (IOException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}
