package org.fekz115.innowisetesttask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.fekz115.innowisetesttask.model.Document;
import org.fekz115.innowisetesttask.service.document.DocumentCreationRequest;
import org.fekz115.innowisetesttask.service.document.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @GetMapping("all")
    Collection<Document> getAll() {
        return documentService.getAll();
    }

    @GetMapping("my")
    Collection<Document> getMy(Principal principal) {
        return documentService.getAllForUser(principal.getName());
    }

    @PostMapping(path = "save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Optional<Document> saveDocument(
            @RequestPart String document,
            @RequestPart MultipartFile file
    ) throws IOException {
        return documentService.saveDocument(objectMapper.readValue(document, DocumentCreationRequest.class), file);
    }

    @GetMapping("getFileName/{id}")
    Optional<String> getFileName(@PathVariable int id) {
        return documentService.getFileName(id);
    }

    @GetMapping("/files/{filename}")
    @ResponseBody
    public ResponseEntity<File> serveFile(@PathVariable String filename) {
        File file = documentService.loadFile(filename);
        Document document = documentService.getByName(filename).orElseThrow();
        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + document.getName() + "\""
                )
                .body(file);
    }

}
