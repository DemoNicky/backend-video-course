package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;

}
