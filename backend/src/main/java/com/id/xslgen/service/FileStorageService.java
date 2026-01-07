package com.id.xslgen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path rootDir;

    public FileStorageService(@Value("${storage.root:storage}") String root) {
        this.rootDir = Paths.get(root).toAbsolutePath().normalize();
    }

    public Path templateDir(long userId, long templateId) {
        return rootDir.resolve("users")
                .resolve(Long.toString(userId))
                .resolve("templates")
                .resolve(Long.toString(templateId));
    }

    public Path xslPath(long userId, long templateId) { return templateDir(userId, templateId).resolve("template.xsl"); }
    public Path xmlPath(long userId, long templateId) { return templateDir(userId, templateId).resolve("input.xml"); }
    public Path pdfPath(long userId, long templateId) { return templateDir(userId, templateId).resolve("output.pdf"); }

    public void ensureDirs(long userId, long templateId) throws IOException {
        Files.createDirectories(templateDir(userId, templateId));
    }

    public void save(long userId, long templateId, Path target, InputStream in) throws IOException {
        ensureDirs(userId, templateId);
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public void writeString(long userId, long templateId, Path target, String content) throws IOException {
        ensureDirs(userId, templateId);
        Files.writeString(target, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public Resource asResource(Path path) {
        try {
            Resource r = new UrlResource(path.toUri());
            if (!r.exists()) throw new RuntimeException("File not found");
            return r;
        } catch (Exception e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    public void deleteTemplateDir(long userId, long templateId) throws IOException {
        Path dir = templateDir(userId, templateId);
        if (!Files.exists(dir)) return;

        Files.walk(dir)
                .sorted((a, b) -> b.getNameCount() - a.getNameCount())
                .forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
    }
}
