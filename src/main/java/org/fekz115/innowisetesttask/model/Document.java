package org.fekz115.innowisetesttask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    int id;
    LocalDateTime creationDate;
    String status;
    Set<User> users;
    String fileName;

}
