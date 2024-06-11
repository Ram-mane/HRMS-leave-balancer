package com.hrms.app.dto.responseDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ResponseDtoWrapper<T> {

    Integer statusCode;
    String message;
    T result;

}
