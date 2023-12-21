package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    private String telp;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;


}
