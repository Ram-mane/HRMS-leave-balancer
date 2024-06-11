package com.hrms.app.dto.responseDto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class JwtResponseDto {

    private String message;
    private boolean success;
    private  String token;
    private EmployeeResponseDto userDetails;



    public JwtResponseDto(String message, boolean success) {
        super();
        this.message = message;
        this.success = success;
    }



    public JwtResponseDto(String message, boolean success, String token, EmployeeResponseDto userDetails) {
        super();
        this.message = message;
        this.success = success;
        this.token = token;
        this.userDetails = userDetails;
    }

}
