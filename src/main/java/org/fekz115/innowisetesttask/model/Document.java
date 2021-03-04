package org.fekz115.innowisetesttask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue
    int id;

    @ManyToMany(cascade = CascadeType.MERGE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Set<User> users;

    LocalDateTime creationDate;
    String status;
    String name;

    @JsonIgnore
    String fileName;

}
