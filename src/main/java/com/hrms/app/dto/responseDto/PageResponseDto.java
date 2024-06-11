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
    Integer pageNo;
    Integer pageSize;
    Integer totalPages;
    Long totalElements;
    Boolean lastPage;

}
