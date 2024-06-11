package com.hrms.app.dto.responseDto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class JwtResponseDto {

    String message;
    Boolean success;
    String token;
    String username;

    public JwtResponseDto(String message, boolean success) {
        super();
        this.message = message;
        this.success = success;
    }

    public JwtResponseDto(String message, boolean success, String token, String username) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.username = username;
    }

}
