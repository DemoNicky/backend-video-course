package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "name cant be null!!!")
    private String nama;

    @NotBlank(message = "email cant be null!!!")
    private String email;

    @NotBlank(message = "telp cant be null!!!")
    private String telp;

    @NotBlank(message = "password cant be null!!!")
    private String password;

}
