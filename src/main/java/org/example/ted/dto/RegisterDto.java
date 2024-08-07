package org.example.ted.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private String phone;
    // image
}
