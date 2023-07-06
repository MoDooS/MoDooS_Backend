package com.study.modoos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email
    @NotBlank(message = "email 입력은 필수입니다.")
    private String email;
    @NotBlank(message = "password 입력은 필수입니다.")
    private String password;

}
