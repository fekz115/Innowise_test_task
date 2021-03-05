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
import java.util.*;
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

    public Optional<Document> saveDocument(DocumentCreationRequest documentCreationRequest, MultipartFile file, User author) throws IOException {
        addAuthor(documentCreationRequest.getUsersIds(), author);
        return Optional.of(
                documentRepository.save(
                        new Document(
                                0,
                                toUsersList(documentCreationRequest.getUsersIds()),
                                LocalDateTime.now(),
                                documentCreationRequest.getStatus(),
                                file.getName(),
                                saveFile(file).getName()
                        )
                )
        );
    }

    public Optional<Document> updateDocument(DocumentUpdateRequest documentUpdateRequest, MultipartFile file, User author) throws IOException {
        Document document = documentRepository.getOne(documentUpdateRequest.getId());
        document.setUsers(toUsersList(documentUpdateRequest.getUsersIds()));
        document.setStatus(documentUpdateRequest.getStatus());
        document.setName(file.getName());
        document.setFileName(saveFile(file).getName());
        return Optional.of(documentRepository.save(document));
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

    private Set<User> toUsersList(List<Integer> userIds) {
        return userIds.stream()
                .map(x -> {
                    User user = new User();
                    user.setId(x);
                    return user;
                })
                .collect(Collectors.toSet());
    }

    private void addAuthor(List<Integer> usersIds, User author) {
        if (!usersIds.contains(author.getId())) {
            usersIds.add(author.getId());
        }
    }

    private File saveFile(MultipartFile multipartFile) throws IOException {
        File file = new File(uploadPath + "/" + UUID.randomUUID().toString());
        multipartFile.transferTo(file);
        return file;
    }
}
