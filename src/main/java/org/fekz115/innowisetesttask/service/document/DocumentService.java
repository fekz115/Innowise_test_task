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
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Value("${documentservice.uploadpath}")
    private String uploadPath;

    public Collection<Document> getAll(DocumentFindRequest documentFindRequest) {
        return filter(
                documentFindRequest.getPage() == null 
                        ? documentRepository.findAll()
                        : documentRepository.findAll(documentFindRequest.getPage()),
                documentFindRequest
        );
    }

    public Collection<Document> getAllForUser(String login, DocumentFindRequest documentFindRequest) {
        return filter(userRepository.findByLogin(login).orElseThrow().getDocuments(), documentFindRequest);
    }

    private Collection<Document> filter(Iterable<Document> documents, DocumentFindRequest documentFindRequest) {
        return StreamSupport.stream(documents.spliterator(), false).filter(document -> {
            if(documentFindRequest != null) {
                if (documentFindRequest.getUsersIds() != null) {
                    if (document.getUsers().stream().map(User::getId).noneMatch(x -> documentFindRequest.getUsersIds().contains(x)))
                        return false;
                }
                if (documentFindRequest.getStatus() != null) {
                    return document.getStatus().equals(documentFindRequest.getStatus());
                }
                if (documentFindRequest.getCreationDate() != null) {
                    return document.getCreationDate().equals(documentFindRequest.getCreationDate());
                }
            }
            return true;
        }).collect(Collectors.toList());
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
