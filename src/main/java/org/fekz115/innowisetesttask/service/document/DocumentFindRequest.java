package org.fekz115.innowisetesttask.service.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DocumentFindRequest {
    private final Pageable page;
    private final LocalDateTime creationDate;
    private final List<Integer> usersIds;
    private final String status;
}
