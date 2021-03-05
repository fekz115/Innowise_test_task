package org.fekz115.innowisetesttask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.fekz115.innowisetesttask.model.Document;
import org.fekz115.innowisetesttask.service.UserService;
import org.fekz115.innowisetesttask.service.document.DocumentCreationRequest;
import org.fekz115.innowisetesttask.service.document.DocumentFindRequest;
import org.fekz115.innowisetesttask.service.document.DocumentService;
import org.fekz115.innowisetesttask.service.document.DocumentUpdateRequest;
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
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("all")
    Collection<Document> getAll(@RequestBody DocumentFindRequest documentFindRequest) {
        return documentService.getAll(documentFindRequest);
    }

    @GetMapping("my")
    Collection<Document> getMy(Principal principal, @RequestBody DocumentFindRequest documentFindRequest) {
        return documentService.getAllForUser(principal.getName(), documentFindRequest);
    }

    @PostMapping(path = "save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Optional<Document> saveDocument(
            @RequestPart String document,
            @RequestPart MultipartFile file,
            Principal principal
    ) throws IOException {
        return documentService.saveDocument(
                objectMapper.readValue(document, DocumentCreationRequest.class),
                file,
                userService.getUserByName(principal.getName()).orElseThrow()
        );
    }

    @PutMapping(path = "save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Optional<Document> updateDocument(
            @RequestPart String document,
            @RequestPart MultipartFile file,
            Principal principal
    ) throws IOException {
        return documentService.updateDocument(
                objectMapper.readValue(document, DocumentUpdateRequest.class),
                file,
                userService.getUserByName(principal.getName()).orElseThrow()
        );
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
