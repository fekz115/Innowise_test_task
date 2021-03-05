package org.fekz115.innowisetesttask.service.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocumentUpdateRequest {
    private final int id;
    private final String status;
    private final List<Integer> usersIds;
}
