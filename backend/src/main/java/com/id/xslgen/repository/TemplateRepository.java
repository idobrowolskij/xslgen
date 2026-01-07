package com.id.xslgen.repository;

import com.id.xslgen.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findAllByOwner_Id(Long ownerId);
    Optional<Template> findByIdAndOwner_Id(Long id, Long ownerId);
}
