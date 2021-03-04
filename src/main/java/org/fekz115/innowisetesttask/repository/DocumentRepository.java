package org.fekz115.innowisetesttask.repository;

import org.fekz115.innowisetesttask.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByFileName(String fileName);

}
