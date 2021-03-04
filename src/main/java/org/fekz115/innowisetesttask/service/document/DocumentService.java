package org.fekz115.innowisetesttask.service.document;

import lombok.RequiredArgsConstructor;
import org.fekz115.innowisetesttask.model.Document;
import org.fekz115.innowisetesttask.model.User;
import org.fekz115.innowisetesttask.repository.DocumentRepository;
import org.fekz115.innowisetesttask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Value("${documentservice.uploadpath}")
    private String uploadPath;

    public Collection<Document> getAll() {
        return documentRepository.findAll();
    }

    public Collection<Document> getAllForUser(String login) {
        return userRepository.findByLogin(login).orElseThrow().getDocuments();
    }

    public Optional<Document> saveDocument(DocumentCreationRequest documentCreationRequest, MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        File dest = new File(uploadPath + "/" + uuid);
        file.transferTo(dest);
        return Optional.of(
                documentRepository.save(
                        new Document(
                                0,
                                documentCreationRequest
                                        .getUsersIds()
                                        .stream()
                                        .map(x -> {
                                            User user = new User();
                                            user.setId(x);
                                            return user;
                                        })
                                        .collect(Collectors.toSet()),
                                LocalDateTime.now(),
                                documentCreationRequest.getStatus(),
                                file.getName(),
                                uuid
                        )
                )
        );
    }

    public Optional<String> getFileName(int id) {
        return documentRepository.findById(id).map(Document::getFileName);
    }


    public File loadFile(String filename) {
        return new File(uploadPath + filename);
    }

    public Optional<Document> getByName(String fileName) {
        return documentRepository.findByFileName(fileName);
    }
}
