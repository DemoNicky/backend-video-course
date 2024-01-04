package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InsertVideoRequest {

    @NotBlank(message = "Judul video tidak boleh kosong")
    private String judulVideo;

    @NotBlank(message = "Link video tidak boleh kosong")
    private String linkVideo;

    @NotNull(message = "Status premium tidak boleh kosong")
    private Boolean isPremium;

}
