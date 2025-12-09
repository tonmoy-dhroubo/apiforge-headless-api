package com.apiforge.contenttype.repository;

import com.apiforge.contenttype.model.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
    Optional<ContentType> findByApiId(String apiId);
    boolean existsByApiId(String apiId);
}
