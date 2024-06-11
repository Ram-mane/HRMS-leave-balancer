package com.hrms.app.dto.responseDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PageResponseDto {

    List<?> content;
    int pageNo;
    int pageSize;
    int totalPages;
    long totalElements;
    boolean lastPage;

}
